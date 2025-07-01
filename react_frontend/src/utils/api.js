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
    get(serverUrl + "/api/user/validate", {"Authorization": "Bearer " + authToken, redirect: 'follow'})

export const loginRequest = (username, password) =>
    request(serverUrl + "/api/login", "POST", {"Content-Type": "application/json"},
        JSON.stringify({"username": username, "password": password}))

export const registerRequest = (username, password) =>
    request(serverUrl + "/api/register", "POST", {"Content-Type": "application/json"},
        JSON.stringify({"username": username, "password": password, "secureRandom": "", "role": "USER"}))

export const getUserRsaKeys = (password, authToken) =>
    request(serverUrl + "/api/user/me/keys", "POST",
        {"Authorization": "Bearer " + authToken, "Content-Type": "application/json"},
        JSON.stringify({"password": password}))

export const getAllFriends = (authToken) =>
    get(serverUrl + "/api/user/me/friends", {"Authorization": "Bearer " + authToken, redirect: 'follow'})

export const getAllUsers = (userId, searchUser, jwt) =>
    get(serverUrl + "/api/users?userid=" + userId + "&search=" + searchUser, {"Authorization": "Bearer " + jwt})

export const addFriend = (id, authToken) =>
    request(serverUrl + "/api/user/me/friend", "POST",
        {"Authorization": "Bearer " + authToken, "Content-Type": "application/json"},
        JSON.stringify({"id": id}))

export const getUserData = (jwtToken) =>
    get(serverUrl + "/api/user/me", {"Authorization": "Bearer " + jwtToken} )

export const sendMessage = (conversationId, authToken, body) =>
    request(serverUrl + "/api/conversations/" + conversationId + "/messages", "POST", {"Authorization": "Bearer " + authToken}, body)

export const getAllMessages = (conversationId, authToken, from) =>
    get(serverUrl + "/api/conversations/" + conversationId + "/messages?from=" + from, {"Authorization": "Bearer " + authToken})

export const updateUserData = (authToken, body) =>
    request(serverUrl + "/api/user/me", "PATCH", {"Authorization": "Bearer " + authToken}, body)

export const getPhoto = (fileId, authToken) =>
    blobRequest(serverUrl + "/api/file/" + fileId, "GET", {"Authorization": "Bearer " + authToken})

export const downloadFile = (fileId) =>
    serverUrl + "/api/file/" + fileId