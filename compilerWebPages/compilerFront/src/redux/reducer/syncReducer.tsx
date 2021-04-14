import {fetchSyncConfigArray, switchSyncToActive, switchSyncToInactive} from "../Action";

const initSyncConfigState = {
    isActive: false,
    name: "",
    configArray: []
}

export const syncConfigReducer = (state: any = initSyncConfigState, action: any) => {
    let payload = action.payload
    switch (action.type) {
        case switchSyncToActive:
            return Object.assign({}, state, {
                isActive: true,
                name: payload.name
            })
        case switchSyncToInactive:
            return Object.assign({}, state, {
                isActive: false,
                name: ""
            })
        case fetchSyncConfigArray:
            console.log('sync config reducer -> fetchSyncConfigArray')
            console.log(payload)
            return Object.assign({}, state, {
                configArray: payload.configArray
            })
        default:
            return state
    }
}
