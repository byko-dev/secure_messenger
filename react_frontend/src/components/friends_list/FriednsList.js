import {useSelector} from "react-redux";
import User from "../../elements/user/User";

const FriendsList = () => {
    const friends = useSelector(state => state.messagesReducer.friends);
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