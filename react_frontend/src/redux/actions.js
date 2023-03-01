import types from "./types";

const setUsers  = items => ({
    type: types.SET_USERS, items
})

const resetUsers = () => ({
    type: types.RESET_USERS
})

const setFriends = items => ({
    type: types.SET_FRIENDS, items
})

const setUserData = item => ({
    type: types.SET_USER_DATA, item
})

const setSelectedUser = item => ({
    type: types.SET_SELECTED_USER, item
})

const setMessages = items => ({
    type: types.SET_MESSAGES, items
})

const addNewMessage = item => ({
    type: types.ADD_NEW_MESSAGE, item
})

const resetPagination = () => ({
    type: types.RESET_PAGINATION
})

const setPage = item => ({
    type: types.SET_PAGE, item
})

const resetState = () => ({
    type: types.RESET_STATE
})

const addFriend = item => ({
    type: types.ADD_FRIEND, item
})

const setNavigation = item => ({
    type: types.SET_NAVIGATION, item
})

export default {
    setUsers, resetUsers, setFriends, setUserData,
    setSelectedUser, resetState, setMessages, addNewMessage,
    resetPagination, setPage, addFriend, setNavigation
}