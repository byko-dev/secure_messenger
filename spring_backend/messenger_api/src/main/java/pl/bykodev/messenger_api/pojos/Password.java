package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Password {
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Password miss requirements of 6-32 characters!")
    @NotNull
    private String password;
}
