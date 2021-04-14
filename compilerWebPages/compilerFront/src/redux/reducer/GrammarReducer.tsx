import {changeGrammar} from "../Action";

const initGrammarState = {
    grammar: ""
}

export const GrammarReducer = (state: any = initGrammarState, action: any) => {
    switch (action.type) {
        case changeGrammar:
            let payload = action.payload
            return Object.assign({}, state, {
                grammar: payload.grammar
            })
        default:
            return state
    }
}

