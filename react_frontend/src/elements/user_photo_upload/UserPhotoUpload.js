import {useEffect, useRef, useState} from "react";
import Utils from "../../utils/Utils";

const UserPhotoUpload = ({handleFilePath, photoUrl, username}) => {

    const inputRef = useRef();
    const imagePreviewRef = useRef();
    const [filePath, setFilePath] = useState();

    useEffect(() => {
        if(filePath){
            let reader = new FileReader();
            reader.readAsDataURL(filePath);
            reader.onload = function(e){
                imagePreviewRef.current.style.backgroundImage = "url("+ e.target.result+")";
            }
            handleFilePath(filePath);
        }
    }, [filePath])

    return(
        <div className="avatar-upload">
            <div className="avatar-edit">
                <input type='file' id="imageUpload" ref={inputRef} accept=".png, .jpg, .jpeg" onChange={e => setFilePath(e.target.files[0])}/>
                <label onClick={() => inputRef.current.click()}><i className="bi bi-pencil-fill"></i></label>
            </div>
            <div className="avatar-preview">
                <div id="imagePreview" ref={imagePreviewRef} style={{backgroundImage: "url(" +photoUrl + ")"}}>
                    {Utils.validateData(photoUrl) || Utils.validateData(filePath)? <> </> :
                        <h1>{username? username.substring(0,2).toUpperCase(): ""}</h1>}
                </div>
            </div>
        </div>
    )
}
export default UserPhotoUpload;