package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username length must be between 4 and 20 symbols")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 4, message = "Password must be at lest 4 symbols")
    private String password;
}