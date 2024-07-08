import Utils from "../../utils/Utils";
import classNames from "classnames";

const UserIcon = ({username, userPhotoUrl, className, dropdown}) => {
    return(
        <div className={classNames("user_icon", className)} style={{backgroundImage: "url("+userPhotoUrl+")"}} data-bs-toggle={dropdown}>
            {Utils.validateData(userPhotoUrl)? <></> :
                <h5>{username.substring(0,2).toUpperCase()}</h5>}
        </div>
    )
}

export default UserIcon;