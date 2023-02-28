const serverUrl = "http://localhost:8080";

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
        JSON.stringify({"username": username, "password": password, "secureRandom": ""}))

export const getUserRsaKeys = (password, authToken) =>
    request(serverUrl + "/user/keys", "POST",
        {"Authorization": "Bearer " + authToken, "Content-Type": "application/json"},
        JSON.stringify({"password": password}))
