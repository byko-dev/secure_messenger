import Utils from "../utils/Utils";
import {downloadFile} from "../utils/api";

const Photo = ({fileId, fileType, fileName}) => {
    return (<>{Utils.isPhoto(fileType)? <img className="media" src={downloadFile(fileId)} alt={fileName} />: <></>}</>)
}
export default Photo;