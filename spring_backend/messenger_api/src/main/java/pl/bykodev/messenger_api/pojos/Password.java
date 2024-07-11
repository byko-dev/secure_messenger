package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Password {
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Password miss requirements of 6-32 characters!")
    @NotNull
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public Password() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
