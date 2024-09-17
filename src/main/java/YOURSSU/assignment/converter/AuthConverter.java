package YOURSSU.assignment.converter;

import YOURSSU.assignment.dto.response.AuthResponse.LoginResponse;
import YOURSSU.assignment.dto.response.AuthResponse.TokenRefreshResponse;

public class AuthConverter {
    public static LoginResponse toLoginResponse(
            String accessToken, String refreshToken, String email) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .build();
    }

    public static TokenRefreshResponse toTokenRefreshResponse(
            String accessToken, String refreshToken) {
        return TokenRefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
