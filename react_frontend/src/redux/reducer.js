import types from "./types";

const INITIAL_STATES = {
    user: {},
    users: [],
    friends: [],
    messages: [],
    selectedUser: {},
    page: 0,
    isShowedNavigation: false
}

function reducer(state = INITIAL_STATES, action){
    switch (action.type){
        case types.SET_USERS:
            return {...state, users: action.items}
        case types.RESET_USERS:
            return {...state, users: []}
        case types.SET_FRIENDS:
            return {...state, friends: action.items}
        case types.RESET_FRIENDS:
            return {...state, friends: []}
        case types.ADD_FRIEND:
            return {...state, friends: [...state.friends, action.item]}
        case types.SET_USER_DATA:
            return {...state, user: action.item}
        case types.RESET_USER_DATA:
            return {...state, user: []}
        case types.SET_SELECTED_USER:
            return {...state, selectedUser: action.item}
        case types.SET_MESSAGES:
            return {...state, messages: action.items.concat(state.messages)}
        case types.ADD_NEW_MESSAGE:
            return {...state, messages: [...state.messages, action.item]}
        case types.SET_PAGE:
            return {...state, page: action.item}
        case types.RESET_PAGINATION:
            return {...state, page: 0}
        case types.SET_NAVIGATION:
            return {...state, isShowedNavigation: action.item}
        case types.RESET_STATE:
            return INITIAL_STATES;
        default:
            return state;
    }
}

export default reducer;