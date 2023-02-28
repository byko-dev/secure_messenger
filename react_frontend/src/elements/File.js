import Utils from "../utils/Utils";
import * as api from "../utils/api";

const File = ({fileName, fileType, fileId}) => {
    return(<>{Utils.isNotMediaFile(fileType, fileId)?
        <div className="file">
            <a href={api.downloadFile(fileId)} target="_blank" rel="noreferrer">
                <i className="bi bi-file-earmark-arrow-down-fill"></i>
                {fileName}
            </a>
        </div>: <></>}
    </>)
}
export default File;