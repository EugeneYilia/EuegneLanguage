import {switchCompareToActive, switchCompareToInactive} from "../Action";

const initCompareConfigState  = {
    isActive : false,
    name : ""
}

export const compareConfigReducer = (state: any = initCompareConfigState, action: any) => {
    let payload = action.payload
    switch (action.type) {
        case switchCompareToActive:
            return Object.assign({}, state, {
                isActive: true,
                name: payload.name
            })
        case switchCompareToInactive:
            return Object.assign({}, state, {
                isActive: false,
                name: ""
            })
        default:
            return state
    }
}
