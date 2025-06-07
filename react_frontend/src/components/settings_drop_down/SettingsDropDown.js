import UserPhotoUpload from "../../elements/user_photo_upload/UserPhotoUpload";
import StorageUtils from "../../utils/StorageUtils";
import CookiesUtils from "../../utils/CookiesUtils";
import {resetState} from "../../redux/operations";
import * as api from "../../utils/api";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Utils from "../../utils/Utils";

export const Line = () => <li><hr className="dropdown-divider" /></li>

const SettingsDropDown = () => {

    const userData = useSelector(state => state.messagesReducer.user);
    const [customUsername, setCustomUsername] = useState("");
    const [userDescription, setUserDescription] = useState("");
    const [photoUrl, setPhotoUrl] = useState("");
    const [filePath, setFilePath] = useState(null);
    const navigate = useNavigate();
    const dispatcher = useDispatch();

    useEffect( () => {
        if (!Utils.isEmptyObject(userData)) {
            setCustomUsername(userData.customUsername);
            setUserDescription(userData.userDescription);
            setPhotoUrl(userData.photoUrl);
        }
    }, [userData])

    const saveUserData = async () => {
        let formData = new FormData();
        formData.append("file", filePath);
        formData.append("customUsername", customUsername);
        formData.append("description", userDescription);

        await api.updateUserData(CookiesUtils.getAuthToken(), formData)
            .then(response => console.log(response))
            .catch((error) =>
                error.then(error => {
                    if(error.status === 401){
                        navigate("/")
                        CookiesUtils.removeAuthToken();
                        dispatcher(resetState());
                        StorageUtils.resetStorage();
                    }
                }))
    }

    const logOut = () => {
        StorageUtils.resetStorage();
        CookiesUtils.removeAuthToken();
        dispatcher(resetState());
        navigate("/");
    }

    const handleFilePath = (file) => {
        setFilePath(file)
    }

    return(
        <div className="dropdown">
            <div className="profile-icon" data-bs-toggle="dropdown">
                <i className="bi bi-person-circle"></i>
            </div>

            <ul className="dropdown-menu dropdown-menu-dark dropdown-panel" onClick={(event) => event.stopPropagation()}>
                <li className="d-flex justify-content-center">
                    <UserPhotoUpload photoUrl={photoUrl} username={userData.username} handleFilePath={handleFilePath} />
                </li>
                <li style={{textAlign: "center"}}>
                    <p>@{userData.username}</p>
                </li>
                <Line/>
                <li className="dropdown-padding">
                    <label>Your name:</label>
                    <input type="text" placeholder="Set your name..." value={customUsername} onChange={e => setCustomUsername(e.target.value)}/>
                </li>
                <li className="dropdown-padding">
                    <label htmlFor="descriptionTextArea">Your description: </label>
                    <textarea id="descriptionTextArea" rows="4" cols="27" value={userDescription}
                              placeholder="Type your description..." onChange={e => setUserDescription(e.target.value)} />
                </li>
                <li className="d-flex justify-content-center gap-4 pb-2 pt-2">
                    <button type="button" className="btn btn-success" onClick={() => saveUserData()}>Save changes</button>
                    <button type="button" className="btn btn-danger" onClick={() => logOut()}>Log out</button>
                </li>
            </ul>
        </div>
    )
}
export default SettingsDropDown;