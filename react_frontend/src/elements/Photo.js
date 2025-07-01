import Utils from "../utils/Utils";
import {downloadFile} from "../utils/api";

const Photo = ({files}) => {
    return (
        files.map((file) =>
            Utils.isPhoto(file.contentType)? <img key={file.id} className="media mt-2" src={downloadFile(file.id)} alt={file.name} />: ""
        ))
}
export default Photo;