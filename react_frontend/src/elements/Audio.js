import Utils from "../utils/Utils";
import * as api from "../utils/api";

const Audio = ({fileType, fileId}) => {

    return(<>{Utils.isAudio(fileType)?
        <audio className="media" controls>
            <source src={api.downloadFile(fileId)} type={fileType} />
            Your browser does not support the audio element.
        </audio>: <></>
    }</>)
}

export default Audio;