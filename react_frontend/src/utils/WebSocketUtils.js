import SockJS from "sockjs-client";
import {over} from "stompjs";

class WebSocketUtils {

    stompClient = null;
    sockJS = null;
    isConnected = false;
    conversationListSocket = [];


    subscribeUrl = (username, conversationId) => "/user/"+ username +"/conversation/" + conversationId

    connect = () => {
        if(!this.isConnected){
            this.sockJS = new SockJS('http://localhost:8080/ws');
            this.stompClient = over(this.sockJS);

            this.stompClient.connect({}, () => {
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