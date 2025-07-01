import SockJS from "sockjs-client";
import {over} from "stompjs";
import * as api from "./api";
import CookiesUtils from "./CookiesUtils";

class WebSocketUtils {

    stompClient = null;
    sockJS = null;
    isConnected = false;
    conversationListSocket = [];
    alertListSocket = [];


    subscribeUrl = (username, conversationId) => "/socket/"+ username +"/conversation/" + conversationId

    alertNewMessageUrl = (username) => "/socket/" + username + "/alert"

    connect = () => {
        if(!this.isConnected){
            this.sockJS = new SockJS(api.serverUrl + "/ws");
            this.stompClient = over(this.sockJS);

            this.stompClient.connect({Authorization: 'Bearer ' + CookiesUtils.getAuthToken(), "X-AUTH-TOKEN": 'Bearer ' + CookiesUtils.getAuthToken()}, () => {
                this.isConnected = true
            }, this.onError);
        }
    }

    subscribeConversation = (username, conversationId, callback) => {
        //prevent for many subscription for one conversation
        if(!this.conversationListSocket.find(element => element === conversationId && this.isConnected)){
            this.conversationListSocket.push(conversationId)

            this.stompClient.subscribe(this.subscribeUrl(username, conversationId), (message) => {
                callback(JSON.parse(message.body));
            });
        }
    }

    subscribeNotification = (username, callback) => {
        if(!this.alertListSocket.find(element => element === username && this.isConnected)){

            this.alertListSocket.push(username)

            this.stompClient.subscribe(this.alertNewMessageUrl(username), (message) => {
                callback(JSON.parse(message.body));
            });
        }
    }

    disconnectSocket = () => {
        this.stompClient.disconnect( () => {
            console.log("Socket disconnected!")
        });
        this.sockJS.close();
        this.isConnected = false;
    }

    onError = (err) => {
        console.log(err)
    }
}

export default WebSocketUtils;