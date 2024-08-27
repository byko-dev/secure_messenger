import {useRef, useState} from "react";
import CookiesUtils from "../../utils/CookiesUtils";
import * as api from "../../utils/api";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {JSEncrypt} from "jsencrypt";
import {addNewMessage, resetState} from "../../redux/operations";
import StorageUtils from "../../utils/StorageUtils";
import EmojiPicker, {Theme} from "emoji-picker-react";
import Spinner from "../../elements/Spinner";

const MessageInput = () => {

    const [message, setMessage] = useState("");
    const [filePath, setFilePath] = useState(null);
    const uploadFileRef = useRef();
    const [fetchState, setFetchState] = useState(false)
    const navigate = useNavigate();
    const selectedUserData = useSelector(state => state.messagesReducer.selectedUser);
    const author = useSelector(state => state.messagesReducer.user).username;
    const dispatcher = useDispatch();
    const [showEmoji, setShowEmoji] = useState(false);

    const sendMessageEvent =  async () => {
        if ((message != "" || filePath != null) && !fetchState) {
            setFetchState(true);

            const encrypt = new JSEncrypt();
            encrypt.setPublicKey(StorageUtils.getPublicKey());

            let formdata = new FormData();

            if (filePath != null)
            {
                for (let i = 0; i < filePath.length; i++) {
                    formdata.append("files", filePath[i]);
                }
            }

            formdata.append("messageForOwnerStr", encrypt.encrypt(message));

            encrypt.setPublicKey(selectedUserData.publicKey);
            formdata.append("messageForFriendStr", encrypt.encrypt(message));
            formdata.append("author", author)

            await api.sendMessage(selectedUserData.conversationId, CookiesUtils.getAuthToken(), formdata)
                .then(async (response) => {
                    setFetchState(false);
                    setMessage("");
                    setFilePath(null);
                    await dispatcher(addNewMessage(response));
                }).catch((error) =>
                    error.then(error => {
                        if(error.status === 401){
                            navigate("/")
                            CookiesUtils.removeAuthToken();
                            dispatcher(resetState());
                            StorageUtils.resetStorage();
                        }
                    }))
        }
    }

    const resetUploadValue = () => {
        setFilePath([]);
        uploadFileRef.current.value = null;
    }
    const confirmByEnter = (event) => {
        if(event.keyCode === 13) sendMessageEvent()
    }

    return(
        <div className="message-input-container">

            <div className="input-modules d-flex justify-content-end">

                {showEmoji?
                    <div>
                        <EmojiPicker
                            onEmojiClick={(emojiData) => setMessage(context => context + emojiData.emoji)}
                            autoFocusSearch={false}
                            theme={Theme.DARK}
                        />
                    </div>: <></>
                }

                <div className="d-flex gap-3 flex-wrap">
                    {filePath != null ?
                        (() => {
                            const buttons = [];
                            for (let index = 0; index < filePath.length; index++) {
                                const file = filePath[index];
                                buttons.push(
                                    <button type="button" className="btn btn-primary" style={{zIndex: "1"}} onClick={() => resetUploadValue()} key={index}>
                                        {file.name} <span className="badge text-bg-secondary">X</span>
                                    </button>
                                );
                            }
                            return buttons;
                        })()
                        : ""
                    }
                </div>

                <div className="wrapper">

                    <div className="input-box">
                        <input type="text" placeholder="Message" value={message} onKeyDown={(e) => confirmByEnter(e)}
                               onChange={e => setMessage(e.target.value)}/>
                        <i className="bi bi-emoji-smile" onClick={() => setShowEmoji(!showEmoji)}></i>
                        <i className="bi bi-paperclip" onClick={() => uploadFileRef.current.click()}></i>
                        <input type="file" ref={uploadFileRef} onChange={e => setFilePath(e.target.files)} multiple/>
                    </div>

                    <div className="button-wrapper" onClick={() => sendMessageEvent()}>
                    {fetchState == false? <i className="bi bi-send-fill"></i>:
                            <Spinner classNameElement2={"d-none"} />
                        }
                    </div>

                </div>
            </div>
        </div>

    )
}

export default MessageInput;