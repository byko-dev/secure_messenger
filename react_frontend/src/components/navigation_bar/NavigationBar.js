import {useDispatch, useSelector} from "react-redux";
import SettingsDropDown from "../settings_drop_down/SettingsDropDown";
import {setNavigation} from "../../redux/operations";
import classNames from "classnames";
import SearchBar from "../search_bar/SearchBar";
import FriendsList from "../friends_list/FriednsList";

const NavigationBar = () => {

    const isVisible = useSelector(state => state.messagesReducer.isShowedNavigation);
    const dispatcher = useDispatch();

    return(
        <div className={classNames("navigation-bar", isVisible? "active-navigation": "")} >
            <div className="menu">
                <SettingsDropDown />
                <SearchBar />
                <div className={"back-arrow"} onClick={() => dispatcher(setNavigation(false))}>
                    <i className="bi bi-arrow-left"></i>
                </div>
            </div>
            <FriendsList />
        </div>
    )
}
export default NavigationBar;