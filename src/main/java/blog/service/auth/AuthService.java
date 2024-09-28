package blog.service.auth;

import blog.dto.request.AuthRequest.*;
import blog.dto.response.AuthResponse.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);
}
