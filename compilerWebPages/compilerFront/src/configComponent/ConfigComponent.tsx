import React from "react";
import {Button, Input, Select, Table, Space} from "antd";
import Highlighter from "react-highlight-words";
import {SearchOutlined} from '@ant-design/icons';
import axios from "axios";
import {SERVER_URL} from "../config";
import "./ConfigComponent.less";
import moment from "moment";
import {ConfigSnapshot, generateIdString, handleResponse, isXmlConfig, reLogin} from "../GlobalBus";
import {connect} from "react-redux";
import {
    appendConfigLog,
    environmentChange,
    versionChange,
    importButtonSwitchToFinished,
    importButtonSwitchToLoading, switchSyncToActive, switchCompareToActive, fetchSyncConfigArray
} from "../redux/Action";

class ConfigComponent extends React.Component {

    // 本地状态用state进行存储
    // 共享状态用redux store进行存储
    state = {
        versionOptions: [],
        configs: [],
        searchText: '',
        searchedColumn: '',
        // userInfo: {
        //     name: undefined,
        //     avatar: undefined
        // }
    }

    private interval: number | undefined;

    renderVersionOptions = (versionArray: any) => versionArray ? versionArray.map((item: string) => {
        return (<Select.Option value={item}>{item}</Select.Option>)
    }) : null

    handleEnvironmentChange = (newValue: String) => {
        // this.setState({
        //     currentEnvironment: newValue
        // });
        // console.log(`environment:${this.state.currentEnvironment}   version:${this.state.currentVersion}`)

        // @ts-ignore
        this.props.dispatchEnvironmentChange({
            "type": environmentChange,
            "payload": {
                "value": newValue
            }
        })
    }

    handleVersionChange = (newValue: String) => {
        // this.setState({
        //     currentVersion: newValue
        // });
        // console.log(`environment:${this.state.currentEnvironment}   version:${this.state.currentVersion}`)

        // @ts-ignore
        this.props.dispatchVersionChange({
            "type": versionChange,
            "payload": {
                "value": newValue
            }
        })
    }

    // @ts-ignore
    checkChoose = () => this.props.currentEnvironment !== 'X' && this.props.currentVersion !== 'X'

    updateConfig = () => {
        let that = this
        axios({
            method: 'get',
            // @ts-ignore
            url: SERVER_URL + "/config-tool-server/fetch_configs/?server_version=" + this.props.currentVersion + "&game_environment=" + this.props.currentEnvironment,
        }).then((response: any) => {
            // console.log(response.data.configs)
            let configArray: any = []
            response.data.configs.forEach((configJson: any) => configArray.push({'config': configJson['config']}))
            // this.setState({
            //     configs: configArray
            // })
            let queryStr = generateIdString()
            axios({
                method: 'post',
                url: SERVER_URL + `/config-tool-server/web_check_configs?${queryStr}`,
                data: JSON.stringify({
                    // @ts-ignore
                    environment: this.props.currentEnvironment,
                    // @ts-ignore
                    version: this.props.currentVersion,
                    config_array: configArray,
                })
            }).then((response: any) => {
                // console.log(response)
                let newConfigArray: { config: string; localNotHas: boolean; }[] = []
                let responseData: [] | undefined = response.data.data
                // console.log(responseData)
                if (responseData === undefined) {
                    reLogin()
                } else {
                    responseData.forEach((responseData: any) => {
                        newConfigArray.push({
                            "config": responseData.config,
                            "localNotHas": responseData.localNotHas
                        })
                    })
                }
                this.fetchOperationInfo(newConfigArray)
            })
        })
    }

    importConfig = (name: any) => {
        // @ts-ignore
        this.props.dispatchSwitchImportButtonToLoading({
            "type": importButtonSwitchToLoading,
            "payload": {"name": name}
        })
        let requestUrl: string
        let queryStr = generateIdString()
        if (isXmlConfig(name)) {
            requestUrl = SERVER_URL + `/config-tool-server/web_xml_import?${queryStr}`
        } else {
            requestUrl = SERVER_URL + `/config-tool-server/web_import?${queryStr}`
        }
        let snapshot: ConfigSnapshot = {
            // @ts-ignore
            environment: this.props.currentEnvironment,
            // @ts-ignore
            version: this.props.currentVersion,
            filename: name,
            result: ''
        };

        // @ts-ignore
        let userInfo = this.props.userInfo
        axios({
            method: 'post',
            url: requestUrl,
            data: JSON.stringify({
                // @ts-ignore
                environment: this.props.currentEnvironment,
                // @ts-ignore
                version: this.props.currentVersion,
                filename: name,
                name: userInfo.name,
            })
        }).then((response: any) => {
            // console.log(response)
            handleResponse(response)
            let responseData = response.data
            if (responseData.data === 'success') {
                snapshot['result'] = responseData.data
                console.log(`upload ${name} successful`)
                this.appendConfigLog(snapshot)
            }
            // @ts-ignore
            this.props.dispatchSwitchImportButtonToFinished({
                "type": importButtonSwitchToFinished,
                "payload": {"name": name}
            })
        })
    }

    fetch_operation_version_list = (env: any, version: any, name: any) => {
        axios({
            method: 'post',
            url: SERVER_URL + "/config-tool-server/fetch_operation_version_list",
            data: JSON.stringify({
                environment: env,
                version: version,
                filename: name,
            })
        }).then((response: any) => {
            let returnResult = JSON.parse(response.data)
            console.log("returnResult")
            console.log(returnResult)
            // @ts-ignore
            this.props.dispatchSyncConfigArray({
                type: fetchSyncConfigArray,
                payload: {
                    configArray: returnResult
                }
            })
        })
    }

    syncConfig = (name: any) => {
        // @ts-ignore
        this.props.dispatchSwitchSyncActive(
            {
                type: switchSyncToActive,
                payload: {
                    name: name,
                    // @ts-ignore
                    environment: this.props.currentEnvironment,
                    // @ts-ignore
                    version: this.props.currentVersion,
                }
            }
        )

        // @ts-ignore
        this.fetch_operation_version_list(this.props.currentEnvironment, this.props.currentVersion, name)

    }

    compareConfig = (name: any) => {
        // @ts-ignore
        this.props.dispatchSwitchCompareActive(
            {
                type: switchCompareToActive,
                payload: {
                    name: name
                }
            }
        )
    }

    appendConfigLog = (snapshot: ConfigSnapshot) => {
        // @ts-ignore
        this.props.dispatchAppendLog({
            type: appendConfigLog,
            payload: {
                "environment": snapshot.environment,
                "version": snapshot.version,
                "filename": snapshot.filename,
                "result": snapshot.result
            }
        })
    }


    private getRowClassName = (_: any, index: number) => {
        return index % 2 === 0 ? "evenRow" : "oddRow"
    }

    tick = () => {
        if (this.checkChoose()) {
            this.updateConfig()
            // @ts-ignore
            // if(this.props.currentEnvironment !== "Test") {
            //
            // } else {
            // }
        }

    }

    fetchOperationInfo = (configArray: any) => {
        axios({
            method: 'post',
            url: SERVER_URL + "/config-tool-server/fetch_operator_info",
            data: JSON.stringify({
                // @ts-ignore
                environment: this.props.currentEnvironment,
                // @ts-ignore
                version: this.props.currentVersion,
            })
        }).then((response: any) => {
                // console.log(response)
                let responseData = response.data
                console.log(responseData)
                let [...newConfigArray] = configArray
                responseData.forEach((data: any) => {
                    newConfigArray.forEach((element: any) => {
                        if (element.config === data.name) {
                            element.operator = data.operator
                            element.time = data.time
                        }
                    })
                })
                this.setState({
                    'configs': newConfigArray
                })
            }
        )
    }


    private fetchVersionList = () => {
        axios({
            method: 'get',
            url: SERVER_URL + "/config-tool-server/fetch_versions/",
        }).then((response: any) => {
            let returnVersionArray: [] = response.data.versions
            let tempArray: string[] = []
            returnVersionArray.forEach((versionJson: any) => tempArray.push(versionJson['version']))
            this.setState({
                versionOptions: tempArray
            })
        })
    }


    componentDidMount() {
        this.fetchVersionList()

        this.interval = window.setInterval(() => this.tick(), 3000);
    }

    componentWillUnmount() {
        if (this.interval !== undefined) {
            clearInterval(this.interval);
        }
    }

    render() {
        // columns sorter multiple的值越大  以该列的sorter进行排序的优先级就越高
        const testColumns = [{
            title: "配置名称",
            dataIndex: "config",
            key: "config",
            defaultSortOrder: 'ascend' as 'ascend',
            sorter: {
                compare: (first: any, second: any) => {
                    if (first.localNotHas === second.localNotHas) {
                        return first.config.localeCompare(second.config)
                    }
                    if (first.localNotHas === true) {
                        return 1
                    } else {
                        return -1
                    }
                }
                ,
                multiple: 999
            },
            ...this.getColumnSearchProps('config'),
            width: "100px"
        },
            //     {
            //     title: "配置版本",
            //     dataIndex: "configVersion",
            //     key: "configVersion",
            //     sorter: {
            //         compare: (first: any, second: any) => first.configVersion - second.configVersion,
            //         multiple: 998
            //     },
            //     width: "100px"
            //     // ...this.getColumnSearchProps('configName'),
            // },
            //     {
            //     title: "导入人员",
            //     dataIndex: "importOperator",
            //     key: "importOperator",
            //     sorter: {
            //         compare: (first: any, second: any) => first.operator.localeCompare(second.operator),
            //         multiple: 997
            //     },
            //     ...this.getColumnSearchProps('importOperator'),
            //     width: "100px"
            // }, {
            //     title: "导入时间",
            //     dataIndex: "importTime",
            //     key: "importTime",
            //     render: (data: any) => (
            //         <>
            //             {data === undefined ? "" : moment(data * 1000).format('YYYY-MM-DD HH:mm:ss')}
            //         </>
            //     ),
            //     width: "100px",
            //     sorter: {
            //         compare: (first: any, second: any) => first.importTime - second.importTime,
            //         multiple: 2
            //     },
            // },
            {
                title: "同步人员",
                dataIndex: "operator",
                key: "operator",
                sorter: {
                    compare: (first: any, second: any) => first.operator.localeCompare(second.operator),
                    multiple: 996
                },
                width: "100px",
                ...this.getColumnSearchProps('operator'),
            }, {
                title: "同步时间",
                dataIndex: "time",
                key: "time",
                render: (data: any) => (
                    <>
                        {data === undefined ? "" : moment(data * 1000).format('YYYY-MM-DD HH:mm:ss')}
                    </>
                ),
                width: "100px",
                sorter: {
                    compare: (first: any, second: any) => first.time - second.time,
                    multiple: 1
                },
            }, {
                title: "操作",//Import Sync Compare
                key: "action",
                // dataIndex: "localNotHas",
                width: "200px",
                render: (data: any) => (
                    <>
                        {
                            // @ts-ignore
                            this.props.currentEnvironment !== 'Test' &&
                            // @ts-ignore
                            <Button loading={this.props.importButtonLoading[data.config] !== undefined}
                                    disabled={data.localNotHas}
                                    onClick={() => this.importConfig(data.config)}>导入</Button>
                        }
                        {
                            // @ts-ignore
                            this.props.currentEnvironment === 'Test' &&
                            <Button disabled={data.localNotHas} onClick={() => this.syncConfig(data.config)}>同步</Button>
                        }
                        {
                            // @ts-ignore
                            this.props.currentEnvironment === 'Test' &&
                            <Button disabled={data.localNotHas}
                                    onClick={() => this.compareConfig(data.config)}>对比</Button>
                        }
                    </>
                )
            }]

        const nonTestColumns = [{
            title: "配置名称",
            dataIndex: "config",
            key: "config",
            defaultSortOrder: 'ascend' as 'ascend',
            sorter: {
                compare: (first: any, second: any) => {
                    if (first.localNotHas === second.localNotHas) {
                        return first.config.localeCompare(second.config)
                    }
                    if (first.localNotHas === true) {
                        return 1
                    } else {
                        return -1
                    }
                },
                multiple: 999
            },
            width: "100px",
            ...this.getColumnSearchProps('config'),
        },
            //     {
            //     title: "配置版本",
            //     dataIndex: "configVersion",
            //     key: "configVersion",
            //     sorter: {
            //         compare: (first: any, second: any) => first.configVersion - second.configVersion,
            //         multiple: 998
            //     },
            //     width: "100px"
            //     // ...this.getColumnSearchProps('configName'),
            // },
            {
                title: "导入人员",
                dataIndex: "operator",
                key: "operator",
                sorter: {
                    compare: (first: any, second: any) => first.operator.localeCompare(second.operator),
                    multiple: 997
                },
                ...this.getColumnSearchProps('operator'),
                width: "100px"
            }, {
                title: "导入时间",
                dataIndex: "time",
                key: "time",
                render: (data: any) => (
                    <>
                        {data === undefined ? "" : moment(data * 1000).format('YYYY-MM-DD HH:mm:ss')}
                    </>
                ),
                width: "100px",
                sorter: {
                    compare: (first: any, second: any) => first.time - second.time,
                    multiple: 2
                },
            }, {
                title: "操作",//Import Sync Compare
                key: "action",
                // dataIndex: "localNotHas",
                width: "200px",
                render: (data: any) => (
                    <>
                        {
                            // @ts-ignore
                            this.props.currentEnvironment !== 'Test' &&
                            // @ts-ignore
                            <Button loading={this.props.importButtonLoading[data.config] !== undefined}
                                    disabled={data.localNotHas}
                                    onClick={() => this.importConfig(data.config)}>导入</Button>
                        }
                        {
                            // @ts-ignore
                            this.props.currentEnvironment === 'Test' &&
                            <Button disabled={data.localNotHas} onClick={() => this.syncConfig(data.config)}>同步</Button>
                        }
                        {
                            // @ts-ignore
                            this.props.currentEnvironment === 'Test' &&
                            <Button disabled={data.localNotHas}
                                    onClick={() => this.compareConfig(data.config)}>对比</Button>
                        }
                    </>
                )
            }]

        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const mockData = [
            {
                "config": "C",
                "configVersion": 3,
                "importTime": 1615430262,
                "syncTime": 1615430262,
                "importOperator": "程策",
                "syncOperator": "刘钊",
                "localNotHas": true
            }, {
                "config": "A",
                "configVersion": 1,
                "importTime": 1615430262,
                "syncTime": 1615430262,
                "localNotHas": true
            }, {
                "config": "B",
                "configVersion": 2,
                "importTime": 1615430262,
                "syncTime": 1615430262,
                "importOperator": "程策",
                "syncOperator": "刘钊",
                "localNotHas": true
            }
        ]


        return (
            <div className='configComponent'>
                <div className='chooserContainer'>
                    <div className='chooser'>
                        选择环境:
                        <Select defaultValue="DEV" onChange={this.handleEnvironmentChange} style={{width: "80px"}}>
                            <Select.Option value="DEV">DEV</Select.Option>
                            <Select.Option value="Test">Test</Select.Option>
                            <Select.Option value="Banshu">Banshu</Select.Option>
                            <Select.Option value="PRIVATE">PRIVATE</Select.Option>
                            <Select.Option value="Profiler">Profiler</Select.Option>
                        </Select>
                    </div>
                    <div className='chooser'>
                        选择版本:
                        <Select defaultValue="请选择" onChange={this.handleVersionChange}>
                            {this.renderVersionOptions(this.state.versionOptions)}
                        </Select>
                    </div>
                </div>

                {
                    // @ts-ignore
                    this.props.currentEnvironment === 'Test' ? (
                            <Table columns={testColumns} dataSource={this.state.configs}
                                   rowClassName={this.getRowClassName} scroll={{x: "max-content"}}/>) :
                        (<Table columns={nonTestColumns} dataSource={this.state.configs}
                                rowClassName={this.getRowClassName} scroll={{x: "max-content"}}/>)
                }


                {
                    /*
                    this.state.currentEnvironment === 'Test' ? (
                            <Table columns={testColumns} dataSource={mockData} rowClassName={this.getRowClassName}/>) :
                        (<Table columns={nonTestColumns} dataSource={mockData} rowClassName={this.getRowClassName}/>)
                     */
                }

            </div>
        );
    }

    private searchInput: any;


    getColumnSearchProps = (dataIndex: any) => ({
        // @ts-ignore
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    ref={node => {
                        this.searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{width: 188, marginBottom: 8, display: 'block'}}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined/>}
                        size="small"
                        style={{width: 90}}
                    >
                        Search
                    </Button>
                    <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{width: 90}}>
                        Reset
                    </Button>
                    <Button
                        type="link"
                        size="small"
                        onClick={() => {
                            confirm({closeDropdown: false});
                            this.setState({
                                searchText: selectedKeys[0],
                                searchedColumn: dataIndex,
                            });
                        }}
                    >
                        Filter
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered: any) => <SearchOutlined style={{color: filtered ? '#1890ff' : undefined}}/>,
        onFilter: (value: any, record: any) =>
            record[dataIndex]
                ? record[dataIndex].toString().toLowerCase().includes(value.toLowerCase())
                : '',
        onFilterDropdownVisibleChange: (visible: any) => {
            if (visible) {
                setTimeout(() => this.searchInput.select(), 100);
            }
        },
        render: (text: any) =>
            this.state.searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{backgroundColor: '#ffc069', padding: 0}}
                    searchWords={[this.state.searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ''}
                />
            ) : (
                text
            ),
    });

    handleSearch = (selectedKeys: any, confirm: any, dataIndex: any) => {
        confirm();
        this.setState({
            searchText: selectedKeys[0],
            searchedColumn: dataIndex,
        });
    };

    handleReset = (clearFilters: any) => {
        clearFilters();
        this.setState({searchText: ''});
    };
}

function mapDispatchToProps(dispatch: any) {
    return {
        dispatchAppendLog: (appendLogAction: any) => dispatch(appendLogAction),
        dispatchSwitchToLoading: () => dispatch({type: "switchToLoading"}),
        dispatchSwitchToFinished: () => dispatch({type: "switchToFinished"}),
        dispatchSwitchImportButtonToLoading: (action: any) => dispatch(action),
        dispatchSwitchImportButtonToFinished: (action: any) => dispatch(action),
        dispatchEnvironmentChange: (action: any) => dispatch(action),
        dispatchVersionChange: (action: any) => dispatch(action),
        dispatchSwitchSyncActive: (action: any) => dispatch(action),
        dispatchSwitchCompareActive: (action: any) => dispatch(action),
        dispatchSyncConfigArray: (action: any) => dispatch(action)
    }
}

function mapStateToProps(state: any) {
    return {
        userInfo: state.user.userInfo,
        globalLoading: state.globalLoading.isLoading,
        importButtonLoading: state.importButtonLoading,
        currentEnvironment: state.env.currentEnvironment,
        currentVersion: state.env.currentVersion,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ConfigComponent)
