import React from "react";
import {BackTop} from "antd";

const xStyle = {
    back: {
        height: "40px",
        width: "80px",
        lineHeight: '40px',
        borderRadius: "4px",
        backgroundColor: "#1088e9",
        color: 'white',
        fontSize: "14px",
    }
};


class BackTopComponent extends React.Component {


    render() {
        return (
            <BackTop style={{textAlign:"center"}}>
                <div style={xStyle.back}>回到顶部</div>
            </BackTop>
        );
    }
}

export default BackTopComponent
