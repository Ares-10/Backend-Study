package YOURSSU.blog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import YOURSSU.blog.controller.AuthController;
import YOURSSU.blog.dto.request.AuthRequest;
import YOURSSU.blog.dto.response.AuthResponse;
import YOURSSU.blog.global.auth.filter.JwtAuthenticationFilter;
import YOURSSU.blog.global.auth.handler.resolver.AuthUserArgumentResolver;
import YOURSSU.blog.global.auth.provider.JwtTokenProvider;
import YOURSSU.blog.global.exception.GlobalErrorCode;
import YOURSSU.blog.global.exception.GlobalException;
import YOURSSU.blog.service.auth.AuthService;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext context;
    @Autowired private MockMvc mockMvc;
    @MockBean private AuthService authService;
    @MockBean private AuthUserArgumentResolver authUserArgumentResolver;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String LOCAL_DATE_TIME_PATTERN =
            "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{6}";

    @BeforeEach
    public void setup() {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .alwaysDo(print())
                        .build();
    }

    @Test
    public void 로그인() throws Exception {
        // given
        AuthRequest.LoginRequest request =
                new AuthRequest.LoginRequest("email@urssu.com", "password");

        AuthResponse.LoginResponse response =
                new AuthResponse.LoginResponse("email@urssu.com", "access-token", "refresh-token");

        // when
        when(authService.login(any(AuthRequest.LoginRequest.class))).thenReturn(response);

        // then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    public void 로그인_잘못된인증정보() throws Exception {
        // given
        AuthRequest.LoginRequest request =
                new AuthRequest.LoginRequest("email@urssu.com", "wrong-password");

        // when
        when(authService.login(any(AuthRequest.LoginRequest.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.EMAIL_PASSWORD_MISMATCH));

        // then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("이메일에 대한 비밀번호가 일치하지 않습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/auth/login"));
    }

    @Test
    public void 로그인_유저없음() throws Exception {
        // given
        AuthRequest.LoginRequest request =
                new AuthRequest.LoginRequest("email@urssu.com", "wrong-password");

        // when
        when(authService.login(any(AuthRequest.LoginRequest.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.USER_NOT_FOUND));

        // then
        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 사용자를 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/auth/login"));
    }

    @Test
    public void 토큰갱신() throws Exception {
        // given
        AuthRequest.TokenRefreshRequest request =
                new AuthRequest.TokenRefreshRequest("valid-refresh-token");

        AuthResponse.TokenRefreshResponse response =
                new AuthResponse.TokenRefreshResponse("new-access-token", "new-refresh-token");

        // when
        when(authService.refresh(any(AuthRequest.TokenRefreshRequest.class))).thenReturn(response);

        // then
        mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    public void 토큰갱신_유효하지않은리프레시토큰() throws Exception {
        // given
        AuthRequest.TokenRefreshRequest request =
                new AuthRequest.TokenRefreshRequest("invalid-refresh-token");

        // when
        when(authService.refresh(any(AuthRequest.TokenRefreshRequest.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN));

        // then
        mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/auth/refresh"));
    }
}
