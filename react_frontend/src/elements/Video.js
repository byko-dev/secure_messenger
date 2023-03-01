import Utils from "../utils/Utils";
import * as api from "../utils/api";

const Video = ({fileId, fileType}) =>{
    return(<>{Utils.isVideo(fileType)?
        <video className="media" controls>
            <source src={api.downloadFile(fileId)} type={fileType} />
            Your browser does not support the video element.
        </video> : <></>}
    </>)
}
export default Video;