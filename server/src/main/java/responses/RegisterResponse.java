package responses;

import models.Auth;

public record RegisterResponse(Auth auth, String message) {
}
