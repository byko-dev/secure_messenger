import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {addFriend, findUser, resetState} from "../../redux/operations";
import CookiesUtils from "../../utils/CookiesUtils";
import * as api from "../../utils/api";
import StorageUtils from "../../utils/StorageUtils";

const SearchBar = () => {

    const [searchBarValue, setSearchBarValue] = useState("");

    const users = useSelector(state => state.messagesReducer.users);
    const userData = useSelector(state => state.messagesReducer.user);
    const dispatcher = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        if(searchBarValue.length > 4)
            dispatcher(findUser(userData.id ,searchBarValue, CookiesUtils.getAuthToken()));
    }, [searchBarValue])

    const addFriendRequest = () => {
        if(searchBarValue.length >=3){
            const newFriendId = users.find(e => e.username == searchBarValue).id;

            api.addFriend(newFriendId, CookiesUtils.getAuthToken())
                .then(response => {
                    dispatcher(addFriend(response));
                }).catch((error) =>
                error.then(error => {
                    if(error.status === 401){
                        navigate("/")
                        CookiesUtils.removeAuthToken();
                        dispatcher(resetState());
                        StorageUtils.resetStorage();
                    }
                }))
        }
    }

    return(
        <div className="search-container">
            <input type="search" id="search" list="users" value={searchBarValue}
                   onChange={e => setSearchBarValue(e.target.value)} placeholder="Search..." autoFocus required />
            <datalist id="users">
                {users.map((user, i) => {
                    return (<option key={i}> {user.username} </option>)
                })}
            </datalist>
            <button onClick={() => addFriendRequest()}>Add</button>
        </div>
    )

}
export default SearchBar;