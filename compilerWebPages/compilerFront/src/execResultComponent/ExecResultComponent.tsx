import React from "react";
import {Button, Space} from "antd";
import {UnControlled as CodeMirror} from "react-codemirror2";
import {connect} from "react-redux";
import {changeResult} from "../redux/Action";

class ExecResultComponent extends React.Component {

    state = {
        // "testData": "123\n" +
        //     "4\n" +
        //     "5\n" +
        //     "4\n" +
        //     "5\n" +
        //     "233\n" +
        //     "4\n" +
        //     "6\n" +
        //     "-94\n" +
        //     "456\n" +
        //     "\n" +
        //     "进程已结束，退出代码为 0\n"
    }

    clearResult = () => {
        // @ts-ignore
        this.props.dispatchChangeResult({
            type: changeResult,
            payload: {
                result: ""
            }
        })
    }

    render() {
        return (<>
            <div className={"CodeMirrorWrapper"}>
                <div style={{textAlign: "center", paddingBottom: "2%"}}><Space size={59}><span>执行结果</span><Button
                    type={"primary"} onClick={this.clearResult}>结果清空</Button></Space></div>
                <CodeMirror
                    value={
                        // @ts-ignore
                        this.props.result
                    }
                    options={{
                        mode: 'rust',
                        lineNumbers: true,
                        theme: 'erlang-dark',
                        // direction: "ltr"
                    }}
                    onChange={(editor, data, value) => {
                    }}
                />
            </div>
        </>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {
        dispatchChangeResult: (action: any) => dispatch(action),
    }
}

function mapStateToProps(state: any) {
    return {
        result: state.resultReducer.result
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ExecResultComponent)
