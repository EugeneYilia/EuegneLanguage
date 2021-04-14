import React from "react";
import {Button, Modal, notification} from "antd";
import DraggerComponent from "../draggerComponent/DraggerComponent";
import axios from "axios";
import {generateIdString} from "../GlobalBus";
import {SERVER_URL} from "../config";
import {connect} from "react-redux";

class ScriptComponent extends React.Component {

    state = {
        visible: false,
        confirmLoading: false
    }

    refreshServer = () => {
        let queryStr = generateIdString()
        let requestUrl = SERVER_URL + `/config-tool-server/refresh?${queryStr}`
        axios({
            method: 'post',
            url: requestUrl,
            data: JSON.stringify({
                // @ts-ignore
                environment: this.props.currentEnvironment,
                // @ts-ignore
                version: this.props.currentVersion,
                // @ts-ignore
                name: this.props.userInfo.name,
            })
        }).then((response: any) => {
            let responseData = response.data
            notification["success"]({
                message: responseData.data,
            });
        })
    }

    showDragger = () => {
        this.setState({
            visible: true
        });
    }

    handleOk = () => {
        this.setState({
            visible: false
        });
    }

    handleCancel = () => {
        this.setState({
            visible: false
        });
    }

    // @ts-ignore
    checkChoose = () => this.props.currentEnvironment !== 'X' && this.props.currentVersion !== 'X'

    render() {
        return (<div style={{marginTop:"0.6%"}}>
            <Button type="primary" onClick={this.showDragger} disabled={!this.checkChoose()}
                    style={{marginLeft: "2%", marginRight: "1%"}}>选择文件</Button>
            <Button onClick={this.refreshServer} disabled={!this.checkChoose()}>刷新服务器脚本</Button>
            <Modal
                title="选择要上传的脚本"
                visible={this.state.visible}
                onOk={this.handleOk}
                confirmLoading={this.state.confirmLoading}
                onCancel={this.handleCancel}
            >
                <DraggerComponent/>
            </Modal>
        </div>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {}
}

function mapStateToProps(state: any) {
    return {
        currentEnvironment: state.env.currentEnvironment,
        currentVersion: state.env.currentVersion,
        userInfo: state.user.userInfo,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ScriptComponent)
