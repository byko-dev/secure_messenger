import Utils from "../utils/Utils";
import * as api from "../utils/api";

const Audio = ({files}) => {

    return(
        <div className="d-flex flex-column gap-3">
            {files.map((file) =>
                Utils.isAudio(file.contentType)?
                    <audio className="media" controls key={file.id}>
                        <source src={api.downloadFile(file.id)} type={file.contentType} />
                        Your browser does not support the audio element.
                    </audio>: ""
            )}
        </div>)
}

export default Audio;