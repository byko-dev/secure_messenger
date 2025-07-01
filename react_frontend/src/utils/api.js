export const serverUrl = "http://localhost:8080";

function checkResponse(response){
    if (response.status >= 200 && response.status <= 299) return response.json();
    else throw response.json();
}

const get = (url, header) =>
    fetch(url, {method: "GET", headers: header})
        .then(checkResponse)

const request = (url, method, header, body) =>
    fetch(url, {method: method,
        headers: header,
        body: body})
        .then(checkResponse)

const blobRequest = (url, method, headers) =>
    fetch(url, {method: method,
        headers: headers})
        .then(response => {
            if(response.ok) return response.blob();
            else throw response.json()
        })

export const validToken = (authToken) =>
    get(serverUrl + "/user/valid", {"Authorization": "Bearer " + authToken, redirect: 'follow'})

export const loginRequest = (username, password) =>
    request(serverUrl + "/login", "POST", {"Content-Type": "application/json"},
        JSON.stringify({"username": username, "password": password}))

export const registerRequest = (username, password) =>
    request(serverUrl + "/register", "POST", {"Content-Type": "application/json"},
        JSON.stringify({"username": username, "password": password, "secureRandom": "", "role": "USER"}))

export const getUserRsaKeys = (password, authToken) =>
    request(serverUrl + "/user/keys", "POST",
        {"Authorization": "Bearer " + authToken, "Content-Type": "application/json"},
        JSON.stringify({"password": password}))

export const getAllFriends = (authToken) =>
    get(serverUrl + "/user/friends", {"Authorization": "Bearer " + authToken, redirect: 'follow'})

export const getAllUsers = (userId, searchUser, jwt) =>
    get(serverUrl + "/users?userid=" + userId + "&search=" + searchUser, {"Authorization": "Bearer " + jwt})

export const addFriend = (id, authToken) =>
    request(serverUrl + "/user/add/friend", "POST",
        {"Authorization": "Bearer " + authToken, "Content-Type": "application/json"},
        JSON.stringify({"id": id}))

export const getUserData = (jwtToken) =>
    get(serverUrl + "/user", {"Authorization": "Bearer " + jwtToken} )

export const sendMessage = (conversationId, authToken, body) =>
    request(serverUrl + "/messages/" + conversationId, "POST", {"Authorization": "Bearer " + authToken}, body)

export const getAllMessages = (conversationId, authToken, from) =>
    get(serverUrl + "/messages/" + conversationId + "?from=" + from, {"Authorization": "Bearer " + authToken})

export const updateUserData = (authToken, body) =>
    request(serverUrl + "/user/data", "PATCH", {"Authorization": "Bearer " + authToken}, body)

export const getPhoto = (fileId, authToken) =>
    blobRequest(serverUrl + "/file/" + fileId, "GET", {"Authorization": "Bearer " + authToken})

export const downloadFile = (fileId) =>
    serverUrl + "/file/" + fileId