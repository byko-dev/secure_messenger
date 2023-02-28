import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import WebSocketUtils from "../../utils/WebSocketUtils";
import CookiesUtils from "../../utils/CookiesUtils";
import {useEffect} from "react";
import {findUser, getFriends, getUserData} from "../../redux/operations";

const UserPanel = () => {

    const navigate = useNavigate();
    const dispatch = useDispatch();

    async function validateAuthToken() {
        const valid = await CookiesUtils.checkTokenIsValid();
        if (!valid) navigate("/");
    }
    const webSocket = new WebSocketUtils();

    useEffect(() => {
        dispatch(findUser());
        dispatch(getUserData(CookiesUtils.getAuthToken()));
        dispatch(getFriends(CookiesUtils.getAuthToken()));

        validateAuthToken();
    }, []);

    return(
        <section className="user-panel-container">
            <div className="right-panel">
            </div>
        </section>
    )



}
export default UserPanel;