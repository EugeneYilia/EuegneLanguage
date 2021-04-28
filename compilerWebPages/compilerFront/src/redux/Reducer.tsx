import {combineReducers} from "redux";
import {GrammarReducer} from "./reducer/GrammarReducer";
import {CodeReducer} from "./reducer/CodeReducer";
import {ResultReducer} from "./reducer/ResultReducer";


export const author = "EugeneLiu"

const reducerMap = {
    grammarReducer: GrammarReducer,
    codeReducer: CodeReducer,
    resultReducer: ResultReducer
}

export const allReducer = combineReducers(reducerMap)
