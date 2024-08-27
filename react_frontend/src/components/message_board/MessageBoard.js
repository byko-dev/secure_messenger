import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {JSEncrypt} from "jsencrypt";
import useSound from "use-sound";
import {useEffect, useRef, useState} from "react";
import BellSound from "../../assets/Bell.mp3";
import StorageUtils from "../../utils/StorageUtils";
import {addNewMessage, resetState, setMessages, setPage} from "../../redux/operations";
import Utils from "../../utils/Utils";
import CookiesUtils from "../../utils/CookiesUtils";
import * as api from "../../utils/api";
import classNames from "classnames";
import Spinner from "../../elements/Spinner";
import ChatBox from "../../elements/chat_box/ChatBox";

const MessageBoard = ({webSocket}) => {

    const selectedUser = useSelector(state => state.messagesReducer.selectedUser);
    const userData = useSelector(state => state.messagesReducer.user);
    const messagesRedux = useSelector(state => state.messagesReducer.messages);
    const page = useSelector(state => state.messagesReducer.page)
    const navigate = useNavigate();
    const dispatcher = useDispatch();
    const encrypt = new JSEncrypt();
    const [beepMessage] = useSound(BellSound);
    const [fetch, setFetch] = useState(false);
    const [loadMore, setLoadMore] = useState(true);
    const messagesRef = useRef(null);

    const loadMessages = (callback) => {
        setFetch(true)

        api.getAllMessages(selectedUser.conversationId, CookiesUtils.getAuthToken(), page)
            .then((response) => {
                if(response.length < 10) setLoadMore(false);
                setFetch(false);
                dispatcher(setPage(page+10))
                callback(response);
            })
            .catch(error => error.then(err => {
                setFetch(false);
                if (err.status === 401) {

                    navigate("/")
                    CookiesUtils.removeAuthToken();
                    dispatcher(resetState());
                    StorageUtils.resetStorage();
                    webSocket.disconnectSocket();
                }
            }))
    }

    useEffect(() => {
        if (!Utils.isEmptyObject(selectedUser)) {
            webSocket.connect();

            loadMessages((response) => {
                dispatcher(setMessages(response))
                webSocket.subscribeConversation(userData.username, selectedUser.conversationId, async (message) => {
                    await dispatcher(addNewMessage(message));
                    if (message.author !== userData.username) beepMessage();
                    messagesRef.current.lastElementChild.scrollIntoView({behavior: 'smooth', block: 'start'});
                });
            });
        }
    }, [selectedUser])

    const decryptValue = (value) => {
        encrypt.setPrivateKey(StorageUtils.getPrivateKey());
        return encrypt.decrypt(value);
    }

    const loadMoreMessagesEvent = () => {
        if(!fetch) loadMessages((response) => dispatcher(setMessages(response)));
    }

    return(
        <div className={classNames("messages_table_container", Utils.isEmptyObject(selectedUser)?"justify-content-center": "")} /*ref={messagesParent} */>
            <div ref={messagesRef} className="d-flex flex-column">

                {fetch ?
                    <Spinner className={"d-flex gap-2 justify-content-center mt-4"} />:
                    loadMore && !Utils.isEmptyObject(selectedUser)?
                        <div className="load_more align-self-center mt-2" onClick={() => loadMoreMessagesEvent()}>
                            <p>Load more messages</p>
                        </div>:
                        <></>
                }

                {Utils.isEmptyObject(selectedUser)?
                    <div className="start"> <p> Select a chat to start messaging </p> </div>: <></>}

                {messagesRedux.map((message, index) => (
                    <ChatBox content={decryptValue(message.content)}
                             author={message.author}
                             ownerUsername={userData.username}
                             date={message.date}
                             files={message.files}
                             photoUrl={userData.username === message.author? userData.photoUrl: selectedUser.photoUrl}
                             key={index} />
                ))}
            </div>
        </div>
    )

}
export default MessageBoard;