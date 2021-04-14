import React from "react";
import axios from "axios";
import {Button, Modal, Space} from "antd";
import TextArea from "antd/es/input/TextArea";
import {connect} from "react-redux";

class GrammarComponent extends React.Component {

    /**
     * 包含的功能包括
     * 1. 展示默认文法定义
     * 2. 设置新的文法
     */

    state = {
        isChangePartVisible: false,
        newGrammarContent: '',
    }

    // 组件挂载后 去获取当前所使用的文法
    componentDidMount() {
        axios({
            method: 'get',
            url: 'http://localhost:8989/index',
            withCredentials: true,
        }).then((response: any) => {
            console.log(response.data)
            //TODO
        })
    }

    showChangePart = () => {
        this.setState({
            isChangePartVisible: true
        })
    }

    closeChangePart = () => {
        this.setState({
            isChangePartVisible: false
        })
    }

    newGrammarContentChange = (event: any) => {
        // console.log(event.target.value)
        this.setState({
            newGrammarContent: event.target.value
        })
    }

    // 文法更换
    changeGrammar = () => {
        if (this.state.newGrammarContent !== '') {
            axios({
                method: 'get'
            }).then((response: any) => {
                //TODO
            })
        }
    }

    render() {
        return (<>

            <>
                <Modal visible={this.state.isChangePartVisible} onOk={this.changeGrammar}
                       onCancel={this.closeChangePart} okText={'确认更换'} cancelText={'取消更换'}>
                    <div>新文法</div>
                    <TextArea style={{width: "100%", height: "40vh"}}
                              onChange={(e) => this.newGrammarContentChange(e)}/>
                </Modal>


                <div>当前文法</div>
                {/*@ts-ignore*/}
                <TextArea value={this.props.grammar} style={{width: "20%", height: "40vh"}}/>
                <br/>
                <Button onClick={this.showChangePart}>文法替换</Button>
            </>

        </>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {
        dispatchChangeGrammar: (action: any) => dispatch(action),

    }
}

function mapStateToProps(state: any) {
    return {
        grammar: state.grammarReducer.grammar
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GrammarComponent)
