import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import TimeUtils from "../../utils/TimeUtils";
import Utils from "../../utils/Utils";
import UserIcon from "../../elements/user_icon/UserIcon";
import {Line} from "../settings_drop_down/SettingsDropDown";
import {setNavigation} from "../../redux/operations";
import classNames from "classnames";

const UserNavbar = () => {

    const selectedUserData = useSelector(state => state.messagesReducer.selectedUser);
    const [lastActivity, setLastActivity] = useState();
    const isVisible = useSelector(state => state.messagesReducer.isShowedNavigation);
    const dispatcher = useDispatch();


    useEffect(() => {
        setLastActivity(TimeUtils.getLastTimeActivityAgo(selectedUserData.userLastTimeActivity))
    }, [selectedUserData])

    return (
        <header className="user_navbar_container">
            <div className="user_navbar_info_wrapper">
                {!isVisible ? <div className={"list-icon"} onClick={() => dispatcher(setNavigation(true))}>
                    <i className="bi bi-list"></i>
                </div>: <></> }
                {Utils.isEmptyObject(selectedUserData)? <> </>:
                    <>
                        <div className="dropdown">
                            <UserIcon username={selectedUserData.username} userPhotoUrl={selectedUserData.photoUrl} className={"icon"} dropdown={"dropdown"}/>

                            <ul className="dropdown-menu dropdown-menu-dark dropdown-panel" onClick={(event) => event.stopPropagation()}>
                                <li className="d-flex justify-content-center">
                                    <UserIcon username={selectedUserData.username} userPhotoUrl={selectedUserData.photoUrl} className={"dropdown_icon"}> </UserIcon>
                                </li>
                                <li className="text-center">
                                    <p>@{selectedUserData.username}</p>
                                </li>
                                <Line />
                                <li className="dropdown-padding">
                                    <p>Name: {selectedUserData.customUsername}</p>
                                </li>
                                {selectedUserData.userDescription && (
                                    <li className="dropdown-padding">
                                        <p>Profile description: {selectedUserData.userDescription}</p>
                                    </li>
                                )}
                                <li className="dropdown-padding">
                                    <p> Member since: <span> {TimeUtils.getDate(selectedUserData.accountCreatedAt)} </span></p>
                                </li>
                            </ul>
                        </div>

                        <div className="user_navbar_info">
                            {selectedUserData.customUsername === selectedUserData.username? <p> {selectedUserData.username} </p>:
                                <p> {selectedUserData.customUsername} <span>@{selectedUserData.username} </span> </p> }

                            <p>{lastActivity.message}
                                <i className={classNames("bi bi-circle-fill", lastActivity.online? "online": "offline")}></i>
                            </p>
                        </div>
                    </>
                }
            </div>
        </header>
    )
}

export default UserNavbar;