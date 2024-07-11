package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserID {
    @NotNull
    @Size(min = 36, max = 36, message = "ID miss UUID requirements")
    private String id;

    public UserID(String id) {
        this.id = id;
    }

    public UserID() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
