package market.dto;

public record AuthResponse(UserResponse user, AuthToken token) {
}
