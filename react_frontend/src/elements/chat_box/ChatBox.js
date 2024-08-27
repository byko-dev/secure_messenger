import classNames from "classnames";
import TimeUtils from "../../utils/TimeUtils";
import {memo} from "react";
import Audio from "../Audio";
import File from "../File";
import Photo from "../Photo";
import Video from "../Video";
import UserIcon from "../user_icon/UserIcon";

const ChatBox = ({author, content, files, date, photoUrl, ownerUsername}) => {
    const isOwner = () => author===ownerUsername;

    return(
        <div className={classNames("chat-box", isOwner() ? "align-self-end" : "")}>
            {isOwner() ? <></> : <UserIcon username={author} userPhotoUrl={photoUrl}/>}
            <div>
                <Photo files={files}/>
                <Video files={files}/>

                <div
                    className={classNames("chat-box-container", (isOwner() ? "owner-message" : "friend-message"))}>
                    <File files={files}/>
                    <Audio files={files}/>
                    <div className="message-data">
                        <p>{content}</p>
                        <p className="text-white-50 m-0 align-self-end small">{TimeUtils.dateFormatter(date)}</p>
                    </div>
                </div>
            </div>
        </div>
    )
}
export default memo(ChatBox);