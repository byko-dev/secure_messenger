import {applyMiddleware, createStore} from "redux";
import { composeWithDevTools } from "redux-devtools-extension";
import thunk from "redux-thunk";
import messagesReducer from "./reducer";
import {combineReducers} from "redux";

const reducers = combineReducers({
    messagesReducer: messagesReducer
});

//TODO: remove react-devtools-extension in production
const store = createStore(reducers, composeWithDevTools(applyMiddleware(thunk)));
export default store;