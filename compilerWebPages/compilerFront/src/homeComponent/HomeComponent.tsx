import React from "react";
import {Avatar, BackTop, Button, Layout} from "antd";
import './HomeComponent.less';
import background from "../statistics/img/background.png"
import {UserOutlined} from "@ant-design/icons";
import {connect} from "react-redux";
import BackTopComponent from "../backTopComponent/BackTopComponent";
import AnalysisTableComponent from "../analysisTableComponent/AnalysisTableComponent";
import GrammarComponent from "../grammarComponent/GrammarComponent";
import CodeComponent from "../codeComponent/CodeComponent";
import ParserComponent from "../parserComponent";
import ExecResultComponent from "../execResultComponent/ExecResultComponent";
import LexerComponent from "../lexerComponent/LexerComponent";

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

    render() {
        // const {classes} = this.props
        return (
            <Layout>
                <Header>
                    <h2 className="header title">Eugene Compiler</h2>
                </Header>
                <Content style={homeComponentStyle.content}>
                    <div>
                        <div style={{display: "inline-block", width: "33%", verticalAlign: "top"}}>
                            <GrammarComponent/>
                        </div>
                        <div style={{display: "inline-block", width: "33%", verticalAlign: "top"}}>
                            <CodeComponent/>
                        </div>
                        <div style={{display: "inline-block", width: "33%", verticalAlign: "top"}}>
                            <ExecResultComponent/>
                        </div>
                    </div>
                    <div style={{width: "100%"}}>
                        <LexerComponent/>
                    </div>
                    <div>
                        <AnalysisTableComponent/>
                        <ParserComponent/>
                    </div>

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
    return {}
}

export default connect(mapStateToProps)(HomeComponent)
