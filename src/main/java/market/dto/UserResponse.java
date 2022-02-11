package market.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record UserResponse(
        @Positive
        Long userId,
        @NotNull
        @Max(60)
        String firstName,
        @NotNull
        @Max(60)
        String LastName,
        @Email
        String Email,
        @NotNull
        String role,
        Boolean isBlocked) {
}
