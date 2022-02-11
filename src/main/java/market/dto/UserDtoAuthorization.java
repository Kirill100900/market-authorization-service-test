package market.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

public record UserDtoAuthorization(
        @Email
        String email,
        @Min(8)
        String password) {
}
