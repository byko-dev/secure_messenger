import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import WebSocketUtils from "../../utils/WebSocketUtils";
import CookiesUtils from "../../utils/CookiesUtils";
import {useEffect} from "react";
import {getFriends, getUserData} from "../../redux/operations";
import NavigationBar from "../../components/navigation_bar/NavigationBar";
import UserNavbar from "../../components/user_navbar/UserNavbar";
import MessageBoard from "../../components/message_board/MessageBoard";
import MessageInput from "../../components/message_input/MessageInput";

const UserPanel = () => {

    const navigate = useNavigate();
    const dispatch = useDispatch();

    async function validateAuthToken() {
        const valid = await CookiesUtils.checkTokenIsValid();
        if (!valid) navigate("/");
    }
    const webSocket = new WebSocketUtils();

    useEffect(() => {
        dispatch(getUserData(CookiesUtils.getAuthToken()));
        dispatch(getFriends(CookiesUtils.getAuthToken()));

        validateAuthToken();
    }, []);

    return(
        <section className="user-panel-container">
            <NavigationBar />
            <div className="right-panel">
                <UserNavbar />
                <MessageBoard webSocket={webSocket} />
                <MessageInput />
            </div>
        </section>
    )



}
export default UserPanel;