import React from "react";
import {connect} from "react-redux";
import {
    appendConfigLog, importButtonSwitchToLoading,
    switchSyncToActive,
    switchSyncToInactive,
    syncButtonSwitchToFinished
} from "../../redux/Action";
import "./SyncComponent.less"
import {Button, Input, List, notification, Space, Table} from "antd";
import moment from "moment";
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import axios from "axios";
import {SERVER_URL} from "../../config";
import {isXmlConfig} from "../../GlobalBus";
import TextArea from "antd/es/input/TextArea";

class SyncComponent extends React.Component {

    state = {
        searchText: '',
        searchedColumn: '',
        xml_data: "",
        excel_data: "",
        currentSheetIndex: 0,
        previewColumns: undefined,
        previewData: undefined,
        previewSheetNameArray: undefined,
    }

    syncConfig = (_id: any) => {

        axios({
            method: 'post',
            url: SERVER_URL + "/config-tool-server/sync_config_to_test",
            data: JSON.stringify({
                // @ts-ignore
                environment: this.props.currentEnvironment,
                // @ts-ignore
                version: this.props.currentVersion,
                // @ts-ignore
                filename: this.props.syncName,
                // @ts-ignore
                operator: this.props.userInfo.name,
                _id: _id
            })
        }).then((response: any) => {
            console.log(response.data)

            // @ts-ignore
            this.props.dispatchAppendLog({
                type: appendConfigLog,
                payload: {
                    // @ts-ignore
                    "environment": this.props.currentEnvironment,
                    // @ts-ignore
                    "version": this.props.currentVersion,
                    // @ts-ignore
                    "filename": this.props.syncName,
                    "result": response.data.data
                }
            })
            // @ts-ignore
            this.props.dispatchSwitchSyncButtonToFinished({
                "type": syncButtonSwitchToFinished,
                "payload": {"_id": _id}
            })

            notification["info"]({
                // @ts-ignore
                message: `${this.props.syncName}同步完成`,
            });
        })
    }

    previewConfig = (_id: any) => {
        let that = this
        // @ts-ignore
        if (isXmlConfig(this.props.syncName)) {// xml data
            axios({
                method: 'post',
                // @ts-ignore
                url: SERVER_URL + "/config-tool-server/fetch_specific_xml",
                data: JSON.stringify({
                    _id: _id,
                })
            }).then((response: any) => {
                console.log(response.data)
                this.setState({
                    xml_data: response.data.data
                })
            })
        } else {// excel data
            axios({
                method: 'post',
                // @ts-ignore
                url: SERVER_URL + "/config-tool-server/fetch_specific_excel",
                data: JSON.stringify({
                    _id: _id,
                })
            }).then((response: any) => {
                let responseData: any = response.data
                console.log(responseData)
                let sheetNameArray: any[] = []

                responseData.meta.sheets.forEach((element: any) =>
                    sheetNameArray.push(element.title)
                )

                // @ts-ignore
                console.log(responseData.content[0][0])
                console.log(sheetNameArray)

                // let tempColumns: any[] = []
                let finalColumns: any[] = []
                let finalData: any[] = []

                let currentSheetIndex = that.state.currentSheetIndex

                // responseData.content[currentSheetIndex][0].forEach((element: any, index: any) =>
                //     tempColumns.push({
                //         title: element,
                //         dataIndex: index.toString(),
                //         key: element
                //     })
                // )

                // console.log("responseData.content[0]")
                // console.log(responseData.content[0])
                responseData.content.forEach((sheet: any, sheet_index: any) => {
                    let tempData: any[] = []
                    let tempColumns: any[] = []
                    sheet.forEach((row: any, row_index: any) => {
                        if (row_index !== 0) {
                            let current_element = {}
                            row.forEach((data: any, data_index: number) => {
                                // @ts-ignore
                                current_element[data_index.toString()] = data
                            })
                            tempData.push(current_element)
                        } else {
                            row.forEach((element: any, column_index: any) => {
                                tempColumns.push({
                                    title: element,
                                    dataIndex: column_index.toString(),
                                    key: element
                                })
                            })

                        }
                    })
                    finalData.push(tempData)
                    finalColumns.push(tempColumns)
                })


                this.setState({
                    previewColumns: finalColumns,
                    previewData: finalData,
                    previewSheetNameArray: sheetNameArray
                })
            })
        }
    }

    renderPreviewData = (props: any) => {
        // @ts-ignore
        if (props.syncIsActive && isXmlConfig(props.syncName)) {
            return <>
                <TextArea style={{height: "64vh"}} value={this.state.xml_data}/>
            </>
        } else {

            return <>
                {/*显示sheet的标签按钮*/}
                <List dataSource={this.state.previewSheetNameArray}
                      style={{float: "left"}}
                      renderItem={(item, index) => <>
                          {/* @ts-ignore*/}
                          <Button onClick={() => this.showSheetData(index)}>{item}</Button>
                      </>
                      }
                />
                {/*显示sheet内容的表格*/}
                {
                    this.renderPreviewTable()
                }
            </>
        }
    }

    renderPreviewTable = () => {
        if (this.state.previewData !== undefined) {
            return (<>
                {/*@ts-ignore*/}
                <Table columns={this.state.previewColumns[this.state.currentSheetIndex]} dataSource={this.state.previewData[this.state.currentSheetIndex]}
                       scroll={{x: "max-content"}}/>
            </>)
        } else {
            return (<>

            </>)
        }
    }

    showSheetData = (sheet_index: any) => {
        console.log(sheet_index)
        this.setState({
            currentSheetIndex: sheet_index
        })
    }

    render() {

        const columns = [{
            title: "配置名",
            dataIndex: "name",
            key: "name",
            defaultSortOrder: 'ascend' as 'ascend',
            sorter: {
                compare: (first: any, second: any) => {
                    return first.name.localeCompare(second.name)
                },
                multiple: 999
            },
            width: "100px",
            ...this.getColumnSearchProps('name'),
        }, {
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
                compare: (first: any, second: any) => first.importTime - second.importTime,
                multiple: 2
            },
        }, {
            title: "操作",//Import Sync Compare
            key: "action",
            // dataIndex: "localNotHas",
            width: "200px",
            render: (data: any) => (
                <>
                    <Button disabled={data.localNotHas} onClick={() => this.previewConfig(data._id)}>预览</Button>
                    {/*@ts-ignore*/}
                    <Button loading={this.props.syncButtonLoading[data._id] !== undefined}
                            disabled={data.localNotHas} onClick={() => {
                        // @ts-ignore
                        this.props.dispatchSwitchSyncButtonToLoading({
                            "type": importButtonSwitchToLoading,
                            "payload": {"_id": data._id}
                        })
                        this.syncConfig(data._id)
                    }}>同步</Button>
                </>
            )
        }]


        return (
            <>
                {
                    // @ts-ignore
                    this.props.syncIsActive ? (
                        <div className='operateContainer'>
                            <div>
                                <div>
                                    {/* @ts-ignore*/}
                                    <Table columns={columns} dataSource={this.props.configArray}
                                           rowClassName={this.getRowClassName} scroll={{x: "max-content"}}/>
                                </div>
                                <div>
                                    {
                                        this.renderPreviewData(this.props)
                                    }
                                </div>
                            </div>
                            <Button onClick={
                                // @ts-ignore
                                this.props.dispatchSwitchSyncActive
                            }>关闭</Button>
                        </div>
                    ) : (<></>)
                }
            </>
        );
    }

    private searchInput: any;

    private getRowClassName = (_: any, index: number) => {
        return index % 2 === 0 ? "evenRow" : "oddRow"
    }

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
        dispatchSwitchSyncActive: () => dispatch({type: switchSyncToInactive}),
        dispatchAppendLog: (appendLogAction: any) => dispatch(appendLogAction),
        dispatchSwitchSyncButtonToLoading: (action: any) => dispatch(action),
        dispatchSwitchSyncButtonToFinished: (action: any) => dispatch(action),
    }
}

function mapStateToProps(state: any) {
    return {
        syncName: state.sync.name,
        syncIsActive: state.sync.isActive,
        userInfo: state.user.userInfo,
        currentEnvironment: state.env.currentEnvironment,
        currentVersion: state.env.currentVersion,
        configArray: state.sync.configArray,
        syncButtonLoading: state.syncButtonLoading,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(SyncComponent)
