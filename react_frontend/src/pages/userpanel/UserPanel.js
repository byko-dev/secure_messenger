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
import Alert from "react-s-alert";
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';

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
            <NavigationBar webSocket={webSocket} />
            <div className="right-panel">
                <UserNavbar />
                <MessageBoard webSocket={webSocket} />
                <MessageInput />
                <Alert stack={{limit: 3}} />
            </div>
        </section>
    )



}
export default UserPanel;