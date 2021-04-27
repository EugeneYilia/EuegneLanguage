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

class CodeComponent extends React.Component {

    state = {
        "codeContent": "fn assign() {\n" +
            "    a = 2*2;\n" +
            "    println(a);\n" +
            "    a = 5;\n" +
            "\n" +
            "    if(a){\n" +
            "        println(a);\n" +
            "    }else{\n" +
            "        println(233);\n" +
            "    };\n" +
            "\n" +
            "    a = 0;\n" +
            "    b = 4;\n" +
            "    println(b);\n" +
            "\n" +
            "    if(a){\n" +
            "        println(a);\n" +
            "    }else{\n" +
            "        b = 5;\n" +
            "        println(b);\n" +
            "        println(233);\n" +
            "    };\n" +
            "\n" +
            "    println(b);\n" +
            "}\n" +
            "\n" +
            "fn plus() {\n" +
            "    println(3+3);\n" +
            "}\n" +
            "\n" +
            "fn minus() {\n" +
            "    println(6-100);\n" +
            "}\n" +
            "\n" +
            "fn main() {\n" +
            "   println(123);\n" +
            "   assign();\n" +
            "   plus();\n" +
            "   minus();\n" +
            "   println(456);\n" +
            "}\n"
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
            this.props.dispatchChangeGrammar({
                type: changeGrammar,
                payload: {
                    grammar: responseData.data
                }
            })
        })
    }

    executeCode = () => {
        axios({
            method: 'put',
            url: SERVER_URL + '/api/code',
            withCredentials: true,
        }).then((response: any) => {
            let responseData = response.data
            console.log(responseData)
            // @ts-ignore
            this.props.dispatchChangeGrammar({
                type: changeGrammar,
                payload: {
                    grammar: responseData.data
                }
            })
        })
    }

    render() {
        return (<>
            <div className={"CodeMirrorWrapper"}>
                <div style={{textAlign: "center", paddingBottom: "2%"}}><Space size={59}><span>当前代码</span><Button
                    type={"primary"} onClick={this.executeCode}>开始执行</Button></Space></div>
                <CodeMirror
                    value={this.state.codeContent}
                    options={{
                        mode: 'rust',
                        lineNumbers: true,
                        theme: 'erlang-dark',
                        direction: "ltr"
                    }}
                    onChange={(editor, data, value) => {
                        // console.log(value)
                        this.setState({
                            "codeContent": value
                        })
                    }}
                />
            </div>
        </>);
    }
}

export default CodeComponent
