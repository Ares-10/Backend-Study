package YOURSSU.assignment.service.auth;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.AuthConverter;
import YOURSSU.assignment.dto.request.AuthRequest.LoginRequest;
import YOURSSU.assignment.dto.request.AuthRequest.TokenRefreshRequest;
import YOURSSU.assignment.dto.response.AuthResponse.LoginResponse;
import YOURSSU.assignment.dto.response.AuthResponse.TokenRefreshResponse;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 사용자 인증 처리 (이메일, 비밀번호)
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(), request.getPassword()));

        // 인증된 사용자의 정보를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Access Token 및 Refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(request.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.getEmail());

        // 응답 생성 및 반환
        return AuthConverter.toLoginResponse(accessToken, refreshToken, request.getEmail());
    }

    @Override
    @Transactional
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        // Refresh Token 검증
        if (!jwtTokenProvider.isValidToken(refreshToken, "REFRESH")) {
            throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
        }

        // Refresh Token에서 사용자 email 추출
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        // 응답 생성 및 반환
        return AuthConverter.toTokenRefreshResponse(newAccessToken, newRefreshToken);
    }
}
