package pl.bykodev.messenger_api.pojos;

public class Friend {
    private String friendId;
    private String photoId;
    private String publicKey;
    private String conversationId;
    private String username;
    private long userLastTimeActivity;
    private String customUsername;
    private String userDescription;
    private long accountCreatedAt;

    public Friend(String friendId, String photoId, String publicKey, String conversationId, String username, long userLastTimeActivity, String customUsername, String userDescription, long accountCreatedAt) {
        this.friendId = friendId;
        this.photoId = photoId;
        this.publicKey = publicKey;
        this.conversationId = conversationId;
        this.username = username;
        this.userLastTimeActivity = userLastTimeActivity;
        this.customUsername = customUsername;
        this.userDescription = userDescription;
        this.accountCreatedAt = accountCreatedAt;
    }

    public Friend() {
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserLastTimeActivity() {
        return userLastTimeActivity;
    }

    public void setUserLastTimeActivity(long userLastTimeActivity) {
        this.userLastTimeActivity = userLastTimeActivity;
    }

    public String getCustomUsername() {
        return customUsername;
    }

    public void setCustomUsername(String customUsername) {
        this.customUsername = customUsername;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public long getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public void setAccountCreatedAt(long accountCreatedAt) {
        this.accountCreatedAt = accountCreatedAt;
    }
}
