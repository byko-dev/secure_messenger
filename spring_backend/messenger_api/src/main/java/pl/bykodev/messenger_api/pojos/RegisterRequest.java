package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotNull
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Username miss requirements of 6-32 characters")
    private String username;
    @NotNull
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Password miss requirements of 6-32 characters")
    private String password;
    @NotNull
    @Size(max = 64)
    private String secureRandom;

    public RegisterRequest(String username, String password, String secureRandom) {
        this.username = username;
        this.password = password;
        this.secureRandom = secureRandom;
    }

    public RegisterRequest() {
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

    public String getSecureRandom() {
        return secureRandom;
    }

    public void setSecureRandom(String secureRandom) {
        this.secureRandom = secureRandom;
    }
}
