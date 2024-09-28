package YOURSSU.blog.service.auth;

import YOURSSU.blog.dto.request.AuthRequest.*;
import YOURSSU.blog.dto.response.AuthResponse.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);
}
