import React from "react";
import {Avatar, BackTop, Button, Layout} from "antd";
import ScriptComponent from "../scriptComponent/ScriptComponent";
import ConfigComponent from "../configComponent/ConfigComponent";
import LogComponent from "../logComponent/LogComponent";
import './HomeComponent.less';
import background from "../statistics/img/background.png"
import {UserOutlined} from "@ant-design/icons";
import {connect} from "react-redux";
import {reLogin} from "../GlobalBus";
import {ADMINISTRATORS} from "../config";
import BackTopComponent from "../backTopComponent/BackTopComponent";
import CompareComponent from "../operateComponent/compareComponent/CompareComponent";
import SyncComponent from "../operateComponent/syncComponent/SyncComponent";

const {
    Header, Footer, Content,
} = Layout;

const homeComponentStyle = {
    content: {
        backgroundImage: `url(${background})`,
        paddingTop: "1%",
        paddingBottom: "70px"
    },
    generalUserInfo: {
        float: "right",
        marginTop: "20px",
        marginRight: "20px"
    },
    unLoginButton: {
        float: "right",
        marginTop: "16px",
    }
};

class HomeComponent extends React.Component {

    logout = () => {
        reLogin()
    }

    render() {
        // const {classes} = this.props
        return (
            <Layout>
                <Header>
                    <h2 className="header title">P5-Configuration
                        {
                            // @ts-ignore
                            this.props.avatarSrc !== undefined ?
                                <Button ghost={true} size={"small"} onClick={this.logout}
                                    // @ts-ignore
                                        style={homeComponentStyle.generalUserInfo}>注销</Button> :
                                <></>
                        }
                        {
                            // @ts-ignore
                            this.props.avatarSrc !== undefined ?
                                // @ts-ignore
                                <Avatar size="small" icon={<UserOutlined/>} style={homeComponentStyle.generalUserInfo}
                                    // @ts-ignore
                                        src={this.props.avatarSrc}/> :
                                // @ts-ignore
                                <Avatar size="small" icon={<UserOutlined/>} style={homeComponentStyle.generalUserInfo}/>
                        }
                    </h2>
                </Header>
                <Content style={homeComponentStyle.content}>
                    {
                        // @ts-ignore
                        ADMINISTRATORS.indexOf(this.props.name) !== -1 ? <ScriptComponent/> : <></>
                    }
                    <ConfigComponent/>
                    <LogComponent/>
                    <CompareComponent/>
                    <SyncComponent/>
                    {/*<BackTop/>*/}
                    <BackTopComponent/>
                </Content>
                <Footer style={{textAlign: "center", position: 'fixed', height: '70px', bottom: '0px', width: '100%'}}>
                    Powered by React with Ant Design
                </Footer>
            </Layout>
        );
    }
}

function mapStateToProps(state: any) {
    return {
        avatarSrc: state.user.userInfo.avatar,
        name: state.user.userInfo.name
    }
}

export default connect(mapStateToProps)(HomeComponent)
