package responses;

import models.Auth;

public record LoginResponse(Auth auth, String message) {
}
