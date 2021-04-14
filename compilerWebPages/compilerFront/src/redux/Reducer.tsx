import moment from "moment";
import {combineReducers} from "redux";
import {
    appendConfigLog,
    clearLog, environmentChange, importButtonSwitchToFinished,
    importButtonSwitchToLoading,
    login,
    switchToFinished,
    switchToLoading, syncButtonSwitchToFinished, syncButtonSwitchToLoading, versionChange
} from "./Action";
import {syncConfigReducer} from "./reducer/syncReducer";
import {compareConfigReducer} from "./reducer/compareReducer";

// 共享数据方式

const initialLogState = {
    logArray: [],
}

const logReducer = (state: any = initialLogState, action: any) => {
    console.log("logReducer")
    // console.log(action)
    let stateSnapshot = state
    switch (action.type) {
        case appendConfigLog:
            let [...newLogArray] = stateSnapshot.logArray
            let payload = action.payload
            newLogArray.unshift({
                "time": moment().format("YYYY-MM-DD HH:mm:ss"),
                "environment": payload.environment,
                "version": payload.version,
                "filename": payload.filename,
                "result": payload.result
            })
            // stateSnapshot['logArray'] = newLogArray
            return Object.assign({}, state, {logArray: newLogArray})
        case clearLog:
            // stateSnapshot['logArray'] = []
            return Object.assign({}, state, {logArray: []})
        default:
            return state
    }
}

const initialUserState = {
    userInfo: {
        name: undefined,
        avatar: undefined
    }
}

const userReducer = (state: any = initialUserState, action: any) => {
    console.log("userReducer")
    // console.log(JSON.stringify(action))
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    // let stateSnapshot = state
    switch (action.type) {
        case login:
            let payload = action.payload
            return Object.assign({}, state, {
                userInfo: {
                    name: payload.name,
                    avatar: payload.avatar,
                }
            })
        default:
            return state
    }
}

const initGlobalLoadingState = {
    isLoading: false,
}

const globalLoadingStateReducer = (state: any = initGlobalLoadingState, action: any) => {
    console.log("globalLoadingStateReducer")
    switch (action.type) {
        case switchToLoading:
            return Object.assign({}, state, {
                isLoading: true
            })
        case switchToFinished:
            return Object.assign({}, state, {
                isLoading: false
            })
        default:
            return state
    }
}

const initImportButtonLoadingState = {}

const importButtonLoadingStateReducer = (state: any = initImportButtonLoadingState, action: any) => {
    console.log("importButtonStateReducer")
    let payload = action.payload
    let stateSnapshot = state

    switch (action.type) {
        case importButtonSwitchToLoading:
            stateSnapshot[payload.name] = true
            return Object.assign({}, state, stateSnapshot)
        case importButtonSwitchToFinished:
            delete stateSnapshot[payload.name]
            return Object.assign({}, state, stateSnapshot)
        default:
            return state
    }
}

const initSyncButtonLoadingState = {}

const syncButtonLoadingStateReducer = (state: any = initSyncButtonLoadingState, action: any) => {
    console.log("syncButtonLoadingStateReducer")
    let payload = action.payload
    let stateSnapshot = state

    switch (action.type) {
        case syncButtonSwitchToLoading:
            stateSnapshot[payload._id] = true
            return Object.assign({}, state, stateSnapshot)
        case syncButtonSwitchToFinished:
            delete stateSnapshot[payload._id]
            return Object.assign({}, state, stateSnapshot)
        default:
            return state
    }
}

const initGlobalEnvState = {
    currentEnvironment: "DEV",
    currentVersion: "X",
}

const globalEnvStateReducer = (state: any = initGlobalEnvState, action: any) => {
    console.log("globalEnvStateReducer")
    let payload = action.payload
    switch (action.type) {
        case environmentChange:
            return Object.assign({}, state, {
                currentEnvironment: payload.value
            })
        case versionChange:
            return Object.assign({}, state, {
                currentVersion: payload.value
            })
        default :
            return state
    }
}

export const author = "EugeneLiu"

const reducerMap = {
    log: logReducer,
    user: userReducer,
    globalLoading: globalLoadingStateReducer,
    importButtonLoading: importButtonLoadingStateReducer,
    syncButtonLoading: syncButtonLoadingStateReducer,
    env: globalEnvStateReducer,
    sync: syncConfigReducer,
    compare: compareConfigReducer,
}

export const allReducer = combineReducers(reducerMap)
