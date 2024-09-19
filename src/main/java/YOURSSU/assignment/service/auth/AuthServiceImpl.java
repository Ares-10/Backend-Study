package YOURSSU.assignment.service.auth;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.AuthConverter;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.AuthRequest.LoginRequest;
import YOURSSU.assignment.dto.request.AuthRequest.TokenRefreshRequest;
import YOURSSU.assignment.dto.response.AuthResponse.LoginResponse;
import YOURSSU.assignment.dto.response.AuthResponse.TokenRefreshResponse;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.service.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 비밀번호 검증
        User user = userService.getUserByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new GlobalException(GlobalErrorCode.EMAIL_PASSWORD_MISMATCH);
        String accessToken = jwtTokenProvider.createAccessToken(request.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.getEmail());
        return AuthConverter.toLoginResponse(accessToken, refreshToken, request.getEmail());
    }

    @Override
    @Transactional
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        // Refresh Token 검증
        if (!jwtTokenProvider.isValidToken(refreshToken, "REFRESH"))
            throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);

        // Refresh Token에서 사용자 email 추출
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        // 응답 생성 및 반환
        return AuthConverter.toTokenRefreshResponse(newAccessToken, newRefreshToken);
    }
}
