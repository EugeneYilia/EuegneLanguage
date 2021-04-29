import {changeToken} from "../Action";

const initTokenState = {
    tokens: []
}

export const TokenReducer = (state: any = initTokenState, action: any) => {
    switch (action.type) {
        case changeToken:
            let payload = action.payload
            return Object.assign({}, state, {
                tokens: payload.tokens
            })
        default:
            return state
    }
}

