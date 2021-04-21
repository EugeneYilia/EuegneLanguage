import React from "react";

import {UnControlled as CodeMirror} from 'react-codemirror2'
import "./CodeComponent.less"
import 'codemirror/lib/codemirror.js'
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/erlang-dark.css';
import 'codemirror/mode/rust/rust'
import {Button, Space} from "antd";

class CodeComponent extends React.Component {

    state = {
        "testData": "fn assign() {\n" +
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

    codeChange = ()=>{

    }

    render() {
        return (<>
            <div className={"CodeMirrorWrapper"}>
                <div style={{textAlign:"center",paddingBottom:"2%"}}><Space size={59}><span>当前代码</span><Button type={"primary"} onClick={this.codeChange}>代码替换</Button></Space></div>
                <CodeMirror
                    value={this.state.testData}
                    options={{
                        mode: 'rust',
                        lineNumbers: true,
                        theme: 'erlang-dark',
                        direction: "ltr"
                    }}
                    onChange={(editor, data, value) => {
                    }}
                />
            </div>
        </>);
    }
}

export default CodeComponent
