import Utils from "../utils/Utils";
import * as api from "../utils/api";

const Video = ({files}) =>{
    return(files.map((file) =>
            Utils.isVideo(file.contentType)?
                <video className="media" controls key={file.id}>
                    <source src={api.downloadFile(file.id)} type={file.contentType} />
                    Your browser does not support the video element.
                </video>: ""
        )
    )
}
export default Video;