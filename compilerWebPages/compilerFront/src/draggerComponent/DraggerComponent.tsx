import React, {Component} from "react";
import {Upload, message} from "antd";
import {InboxOutlined} from "@ant-design/icons";
import {connect} from "react-redux";
import {SERVER_URL} from "../config";

class DraggerComponent extends Component {

    private buildQueryParam = () => {
        // @ts-ignore
        return `environment=${this.props.currentEnvironment}&version=${this.wrapVersion(this.props.currentVersion)}&name=${this.props.userInfo.name}`
    }

    private wrapVersion = (version: string) => {
        return "v" + version.replace(".", "_");
    }

    render() {
        const props = {
            name: 'file',
            multiple: true,
            action: SERVER_URL + `/config-tool-server/web_upload_single?${this.buildQueryParam()}`,
            onChange(info: any) {
                console.log(info)
                const {status} = info.file;
                if (status !== 'uploading') {
                    console.log(info.file, info.fileList);
                }
                if (status === 'done') {
                    message.success(`${info.file.name} file uploaded successfully.`);
                } else if (status === 'error') {
                    message.error(`${info.file.name} file upload failed.`);
                }
            },
        }

        return (
            <Upload.Dragger {...props}>
                <p className="ant-upload-drag-icon">
                    <InboxOutlined/>
                </p>
                <p className="ant-upload-text">点击或拖动文件至此区域来上传</p>
                {/*<p className="ant-upload-hint"></p>*/}
            </Upload.Dragger>);
    }
}

function mapStateToProps(state: any) {
    return {
        userInfo: state.user.userInfo,
        currentVersion: state.env.currentVersion,
        currentEnvironment: state.env.currentEnvironment,
    }
}

export default connect(mapStateToProps)(DraggerComponent)
