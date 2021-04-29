import React from "react";

import {UnControlled as CodeMirror} from 'react-codemirror2'
import "./CodeComponent.less"
import 'codemirror/lib/codemirror.js'
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/erlang-dark.css';
import 'codemirror/mode/rust/rust'
import {Button, Space} from "antd";
import axios from "axios";
import {SERVER_URL} from "../config";
import {changeCode, changeResult} from "../redux/Action";
import {connect} from "react-redux";

class CodeComponent extends React.Component {

    state = {
        // "codeContent": ""
        // contentKey: 0
    }

    componentDidMount() {
        axios({
            method: 'get',
            url: SERVER_URL + '/api/code',
            withCredentials: true,
        }).then((response: any) => {
            let responseData = response.data
            console.log(responseData)
            // @ts-ignore
            this.props.dispatchChangeCode({
                type: changeCode,
                payload: {
                    code: responseData.data
                }
            })
        })
    }

    changeCode = (newValue: any) => {
        axios({
            method: 'put',
            url: SERVER_URL + '/api/code',
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify({
                code: newValue
            })
        }).then((response: any) => {
            // let responseData = response.data
            // console.log(responseData)
            // @ts-ignore
            this.props.dispatchChangeCode({
                type: changeCode,
                payload: {
                    code: newValue
                }
            })
        })
    }

    executeCode = () => {
        axios({
            method: 'get',
            url: SERVER_URL + '/api/code/execute',
            withCredentials: true,
        }).then((response: any) => {
            let responseData = response.data
            console.log(responseData)
            // @ts-ignore
            this.props.dispatchChangeResult({
                type: changeResult,
                payload: {
                    result: responseData.data
                }
            })
        })
    }

    render() {
        return (<>
            <div className={"CodeMirrorWrapper"}>
                <div style={{textAlign: "center", paddingBottom: "2%"}}><Space size={59}><span>当前代码</span><Button
                    type={"primary"} onClick={this.executeCode}>开始执行</Button></Space></div>
                {/* value相当于是设置了初始值 并不是始终进行着两两绑定 */}
                <CodeMirror
                    value={
                        //@ts-ignore
                        this.props.code
                    }
                    options={{
                        mode: 'rust',
                        lineNumbers: true,
                        theme: 'erlang-dark',
                        // direction: "ltr"
                    }}
                    onChange={(editor, data, value) => {
                        // console.log(value)
                        // this.setState({
                        //     "codeContent": value
                        // })
                        this.changeCode(value)
                    }}
                />
            </div>
        </>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {
        dispatchChangeCode: (action: any) => dispatch(action),
        dispatchChangeResult: (action: any) => dispatch(action),
    }
}

function mapStateToProps(state: any) {
    return {
        code: state.codeReducer.code
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(CodeComponent)
