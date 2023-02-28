import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import WebSocketUtils from "../../utils/WebSocketUtils";
import CookiesUtils from "../../utils/CookiesUtils";
import {useEffect} from "react";
import {findUser, getFriends, getUserData} from "../../redux/operations";
import NavigationBar from "../../components/navigation_bar/NavigationBar";
import UserNavbar from "../../components/user_navbar/UserNavbar";

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
            <NavigationBar />
            <div className="right-panel">
                <UserNavbar />
            </div>
        </section>
    )



}
export default UserPanel;