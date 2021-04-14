import {combineReducers} from "redux";
import {GrammarReducer} from "./reducer/GrammarReducer";


export const author = "EugeneLiu"

const reducerMap = {
    grammarReducer: GrammarReducer,

}

export const allReducer = combineReducers(reducerMap)
