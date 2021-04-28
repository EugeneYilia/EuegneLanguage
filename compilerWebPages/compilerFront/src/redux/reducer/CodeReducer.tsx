import {changeCode} from "../Action";

const initCodeState = {
    code: ""
}

export const CodeReducer = (state: any = initCodeState, action: any) => {
    switch (action.type) {
        case changeCode:
            let payload = action.payload
            return Object.assign({}, state, {
                code: payload.code
            })
        default:
            return state
    }
}

