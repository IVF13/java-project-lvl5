package hexlet.code.app.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank @Email String email,
        @NotBlank String password) {

    public AuthRequest() {
        this(null, null);
    }
}
