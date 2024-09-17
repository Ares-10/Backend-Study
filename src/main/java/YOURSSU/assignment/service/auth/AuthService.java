package YOURSSU.assignment.service.auth;

import YOURSSU.assignment.dto.request.AuthRequest.*;
import YOURSSU.assignment.dto.response.AuthResponse.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);
}
