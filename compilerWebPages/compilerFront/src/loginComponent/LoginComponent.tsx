import React from "react";
import cookie from "react-cookies";
import template from "@babel/template"
import * as qs from 'query-string';
import {CLIENT_URL, SERVER_URL} from "../config";
import axios from "axios";
import {generateIdString, handleResponse, reLogin} from "../GlobalBus";
import {connect} from "react-redux";
import {login} from "../redux/Action";

class LoginComponent extends React.Component {

    state = {}

    componentDidMount() {

        let refreshToken = cookie.load("user_refresh_token")
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        let accessToken = cookie.load("user_access_token")


        if (refreshToken === undefined) {
            let {code} = qs.parse(window.location.search.substring(1))
            if (code !== undefined) {
                console.log("request code")
                axios({
                    method: 'post',
                    url: SERVER_URL + '/config-tool-server/login',
                    data: JSON.stringify({
                        code: code
                    })
                }).then((response: any) => {
                    let responseData = response.data
                    if (responseData.data !== "login successful") {
                        reLogin()
                    } else {
                        handleResponse(response)
                        this.fetchUserInfo()
                    }
                })
            } else {// refreshToken === undefined && code === undefined
                reLogin()
            }
        } else {
            this.fetchUserInfo()
        }
        // const ast = template.ast(`const axios = require("axios");`);
        // console.log("LoginComponent")
        // console.log(ast)

    }

    fetchUserInfo = () => {
        let queryStr = generateIdString()
        axios({
            method: 'get',
            url: SERVER_URL + `/config-tool-server/check_is_admin/?${queryStr}`,
        }).then((response: any) => {
            console.log(response)
            if (response.data === "") {
                reLogin()
            }
            console.log(response.data)
            console.log("fetch user info")

            // @ts-ignore
            this.props.dispatchLogin({
                type: login,
                payload: {
                    name: response.data.data.name,
                    avatar: response.data.data.avatar
                }
            })
        })
    }

    render() {
        return (<></>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {
        dispatchLogin: (loginAction: any) => dispatch(loginAction)
    }
}

export default connect(null, mapDispatchToProps)(LoginComponent);
