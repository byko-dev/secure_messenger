import {useDispatch, useSelector} from "react-redux";
import User from "../../elements/user/User";
import {useEffect} from "react";
import {addFriendOrMoveOnTop} from "../../redux/operations";
import Alert from 'react-s-alert';

const FriendsList = ({webSocket}) => {
    const friends = useSelector(state => state.messagesReducer.friends);
    const user = useSelector(state => state.messagesReducer.user);
    const dispatcher = useDispatch();

    useEffect( () => {
        if (user.username) {
            webSocket.connect();

            setTimeout(() => {
                webSocket.subscribeNotification(user.username, async (response) => {
                    if (response.action === "NEW_MESSAGE")
                    {
                        Alert.success('New Message from user: ' + response.data.username);
                        dispatcher(addFriendOrMoveOnTop(response.data))
                    }
                    else if (response.action === "NEW_FRIEND")
                    {
                        Alert.success("User: " + response.data.username + "added you to friends!")
                        dispatcher(addFriendOrMoveOnTop(response.data))
                    }
                });
            }, 1000)
        }
    }, [user]);

    return(
        <section className="friend-list">
            {friends.map((user, i) => {
                return (
                    <User userData={user} key={user.friendId} />
                )
            })}
        </section>
    )
}
export default FriendsList;