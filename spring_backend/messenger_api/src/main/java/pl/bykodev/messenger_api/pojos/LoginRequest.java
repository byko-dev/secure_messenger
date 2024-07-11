package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {

    @NotNull
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Username miss requirements of 6-32 characters")
    private String username;

    @NotNull
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Password miss requirements of 6-32 characters")
    private String password;


    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
