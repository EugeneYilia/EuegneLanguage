import {applyMiddleware, createStore} from "redux";
import {createLogger} from "redux-logger";
import {allReducer} from "./Reducer";

const logger = createLogger()

export const store = createStore(allReducer, applyMiddleware(logger))
