package pl.bykodev.messenger_api.pojos;

public class AlertDTO {
    private final String NEW_MESSAGE = "NEW_MESSAGE";
    private final String NEW_FRIEND = "NEW_FRIEND";

    private String action;

    private Object $data;

    public AlertDTO setAction(String action) {
        this.action = action;
        return this;
    }

    public String getAction() {
        return action;
    }

    public AlertDTO setData(Object data) {
        this.$data = data;
        return this;
    }

    public Object getData() {
        return $data;
    }

    public AlertDTO setNewMessageAction()
    {
        setAction(NEW_MESSAGE);
        return this;
    }

    public AlertDTO setNewFriendAction()
    {
        setAction(NEW_FRIEND);
        return this;
    }
}