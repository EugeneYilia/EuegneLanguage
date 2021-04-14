import {CLIENT_URL} from "./config";
import cookie from "react-cookies";
import {author} from "./redux/Reducer";
// declare interface UserInfoInterface {
//     USER_REFRESH_TOKEN: string | undefined,
//     USER_ACCESS_TOKEN: string | undefined
// }
//
// export const userInfo: UserInfoInterface = {
//     USER_REFRESH_TOKEN: undefined,
//     USER_ACCESS_TOKEN: undefined
// }

export declare interface ConfigSnapshot {
    environment: string,
    version: string,
    filename: string,
    result: string
}

export declare interface ConfigElement {
    environment: string,
    version: string,
    filename: string,
    result: string,
    time: string
}

export declare interface StateType {
    logArray: ConfigElement[]
}

const xmlConfig = ['counterConfig', 'systemConfig']

export function reLogin() {
    cookie.remove("user_access_token")
    cookie.remove("user_refresh_token")
    window.location.href = `https://open.feishu.cn/open-apis/authen/v1/index?redirect_uri=${CLIENT_URL}&app_id=cli_9d146093edb89108`
}

export function checkValid(): boolean {
    return author === "EugeneLiu"
}

export function generateIdString(): string {
    let queryStr = ""
    if (cookie.load("user_access_token") !== undefined) {
        queryStr += "user_access_token=" + cookie.load("user_access_token") + "&"
    }
    if (cookie.load("user_refresh_token") !== undefined) {
        queryStr += "user_refresh_token=" + cookie.load("user_refresh_token")
    }
    return queryStr
}

export function isXmlConfig(configName: string): boolean {
    return xmlConfig.indexOf(configName) !== -1
}

export function handleResponse(response: any) {
    let responseData = response.data
    if (responseData['user_access_token'] !== undefined) {
        if (responseData['access_expire_time'] !== undefined) {
            cookie.save("user_access_token", responseData['user_access_token'], {
                maxAge: responseData['access_expire_time']
            })
        }
    }
    if (responseData['user_refresh_token'] !== undefined) {
        if (responseData['refresh_expire_time'] !== undefined) {
            cookie.save("user_refresh_token", responseData['user_refresh_token'], {
                maxAge: responseData['refresh_expire_time']
            })
        }
    }
}
