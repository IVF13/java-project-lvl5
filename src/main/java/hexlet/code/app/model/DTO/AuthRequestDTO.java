package hexlet.code.app.model.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String password) {

    public AuthRequestDTO() {
        this(null, null);
    }
}
