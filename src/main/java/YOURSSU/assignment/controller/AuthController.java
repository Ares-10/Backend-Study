package YOURSSU.assignment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import YOURSSU.assignment.dto.request.AuthRequest.*;
import YOURSSU.assignment.dto.response.AuthResponse.*;
import YOURSSU.assignment.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
    private final AuthService authService;

    // 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // 로그인 요청을 처리하고, accessToken과 refreshToken을 반환
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // 토큰 갱신 엔드포인트
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
            @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        // Refresh Token을 통해 새로운 Access Token과 Refresh Token을 반환
        TokenRefreshResponse response = authService.refresh(tokenRefreshRequest);
        return ResponseEntity.ok(response);
    }
}
