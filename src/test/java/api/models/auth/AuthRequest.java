package api.models.auth;

public record AuthRequest(
        String username,
        String password
) {}