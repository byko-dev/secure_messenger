import classNames from "classnames";
import TimeUtils from "../../utils/TimeUtils";
import {memo} from "react";
import Audio from "../Audio";
import File from "../File";
import Photo from "../Photo";
import Video from "../Video";
import UserIcon from "../user_icon/UserIcon";

const ChatBox = ({author, content, fileId, date, photoUrl, ownerUsername, fileType, fileName}) => {
    const isOwner = () => author===ownerUsername;

    return(
        <div className={classNames("chat-box", isOwner()? "align-self-end": "")} >
            {isOwner()? <></>: <UserIcon username={author} userPhotoUrl={photoUrl} />}
            <div >
                <Photo fileName={fileName} fileId={fileId} fileType={fileType} />
                <Video fileId={fileId} fileType={fileType} />

                <div className={classNames("chat-box-container", (isOwner()? "owner-message": "friend-message"))}>
                    <File fileName={fileName} fileId={fileId} fileType={fileType} />
                    <Audio fileType={fileType} fileId={fileId} />
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