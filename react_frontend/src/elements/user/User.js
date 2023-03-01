import {useDispatch} from "react-redux";
import {setSelectedUser, resetPagination} from "../../redux/operations";
import TimeUtils from "../../utils/TimeUtils";
import UserIcon from "../user_icon/UserIcon";


const User = ({userData}) => {

    const dispatcher = useDispatch();

    return(
        <div className="user_container" onClick={() => {
            dispatcher(setSelectedUser(userData))
            dispatcher(resetPagination())
        }}>
            <UserIcon username={userData.username} userPhotoUrl={userData.photoUrl} />

            <div className="user_details">
                <p>{userData.username}</p>
                <p>{TimeUtils.dateFormatter(userData.userLastTimeActivity)}</p>
            </div>
        </div>
    )

}

export default User;