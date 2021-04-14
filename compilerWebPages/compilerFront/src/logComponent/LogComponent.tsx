import React from "react";
import {Button, Divider, List, Typography} from "antd";
import {generalPadding} from "../theme";
import {ConfigElement} from "../GlobalBus";
import {connect} from "react-redux"
import {clearLog} from "../redux/Action";


class LogComponent extends React.Component {

    // state = {
    //     logArray: []
    // }

    // constructor(props: any) {
    //     super(props);

    //     传统redux的调用方式
    //     store.subscribe(this.updateArray)
    // }

    // updateArray = () => {
    //     this.setState({
    //         logArray: store.getState().log.logArray
    //     })
    // }

    render() {
        // let log: StateType = store.getState().log
        // console.log(`state: ${JSON.stringify(store.getState())}`)
        // console.log(`logArray: ${JSON.stringify(log.logArray)}`)
        // console.log(`logArray size: ${log.logArray.length}`)

        // let renderArray: ConfigElement[] = this.state.logArray

        // @ts-ignore
        const logArray: ConfigElement[] = this.props.logArray

        return (<div style={generalPadding}>
            <Divider orientation="left">日志</Divider>
            <Button type="primary" onClick={() => {
                // @ts-ignore
                this.props.dispatchClearLog({
                    type: clearLog,
                })
            }}>清空日志</Button>
            <List
                bordered={true}
                split={true}
                dataSource={logArray}
                key={logArray.length}
                style={{borderColor: 'black'}}
                renderItem={item => (
                    <List.Item key={item.time + item.filename} style={{borderBottomColor: 'black'}}>
                        <List.Item.Meta
                            title={`${item.environment}   ${item.version} `}
                            description={`${item.time}`}
                        >
                        </List.Item.Meta>
                        {item.filename}

                        {
                            item.result === "success" ? ("导入成功") : ("")
                        }
                        {
                            item.result === "fail" ? (<Typography.Text mark>导入失败</Typography.Text>) : ("")
                        }
                        {
                            item.result === "success2" ? ("同步成功") : ("")
                        }
                        {
                            item.result === "fail2" ? (<Typography.Text mark>同步失败</Typography.Text>) : ("")
                        }
                    </List.Item>
                )}
            />
        </div>);
    }
}

function mapStateToProps(state: any) {
    return {
        logArray: state.log.logArray
    }
}

function mapDispatchToProps(dispatch: any) {
    return {
        dispatchClearLog: (appendLogAction: any) => dispatch(appendLogAction)
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(LogComponent)
