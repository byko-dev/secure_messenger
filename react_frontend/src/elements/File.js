import Utils from "../utils/Utils";
import * as api from "../utils/api";

const File = ({files}) => {

    return(files.map((file) =>
        Utils.isNotMediaFile(file.contentType, file.id)?
            <div className="file" key={file.id}>
                <a href={api.downloadFile(file.id)} target="_blank" rel="noreferrer">
                    <i className="bi bi-file-earmark-arrow-down-fill"></i>
                    {file.name}
                </a>
            </div>: ""))
}
export default File;