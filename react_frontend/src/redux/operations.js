import actions from "./actions";
import * as api from "../api";
import Utils from "../utils/Utils";
import CookiesUtils from "../utils/CookiesUtils";

export const findUser = (userid, userStr, jwt) =>
    async (dispatch) => {
        const users = await api.getAllUsers(userid, userStr, jwt);
        dispatch(actions.setUsers(users));
    }

export const getFriends = (authToken) =>
    async (dispatch) => {
        const userFriends = await api.getAllFriends(authToken)
        const response = await updateFriendsData(userFriends)
        dispatch(actions.setFriends(response));
    }

export const updateFriendsData = async (friends) => {
    let friendListUpdated = [];

    for (const friend of friends) {
        let photoUrl = "";
        if (Utils.validateData(friend.photoId))
            await api.getPhoto(friend.photoId, CookiesUtils.getAuthToken())
                .then(blob => photoUrl = URL.createObjectURL(blob))
                .catch(error => console.log(error))
        friendListUpdated.push({...friend, "photoUrl": photoUrl})
    }
    return friendListUpdated;
}

export const addFriend = (friend) =>
    (dispatch) =>
        dispatch(actions.addFriend({...friend, "photoUrl":
                Utils.validateData(friend.photoId)? api.downloadFile(friend.photoId): ""}))


export const getUserData = (authToken) =>
    async (dispatch) => {
        const userData = await api.getUserData(authToken);
        let photoUrl = "";

        if(Utils.validateData(userData.photoId))
            await api.getPhoto(userData.photoId, CookiesUtils.getAuthToken())
                .then(blob => photoUrl = URL.createObjectURL(blob))
                .catch(error => console.log(error))

        dispatch(actions.setUserData({...userData, "photoUrl": photoUrl}));
    }

export const setSelectedUser = (userData) =>
    (dispatch) => {
        dispatch(actions.setSelectedUser(userData))
    }

export const setMessages = (messages) =>
    (dispatch) => {
        dispatch(actions.setMessages(messages))
    }

export const addMessages = (messages) =>
    (dispatch) => {
        dispatch(actions.addMessages(messages))
    }

export const addNewMessage = (message) =>
    (dispatch) => {
        dispatch(actions.addNewMessage(message))
    }

export const resetSelectedUser = () =>
    (dispatch) => {
        dispatch(actions.resetSelectedUser())
    }
export const resetPagination = () =>
    (dispatch) => {
        dispatch(actions.resetPagination())
    }
export const setPage = (page) =>
    (dispatch) => {
        dispatch(actions.setPage(page))
    }

export const setNavigation = (value) =>
    (dispatch) => {
        dispatch(actions.setNavigation(value))
    }
export const resetState = () =>
    (dispatch) => {
        dispatch(actions.resetState())
    }




