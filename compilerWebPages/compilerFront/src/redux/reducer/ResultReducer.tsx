import {changeResult} from "../Action";

const initResultState = {
    result: ""
}

export const ResultReducer = (state: any = initResultState, action: any) => {
    switch (action.type) {
        case changeResult:
            let payload = action.payload
            return Object.assign({}, state, {
                result: payload.result
            })
        default:
            return state
    }
}

