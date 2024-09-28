package YOURSSU.blog;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import YOURSSU.blog.controller.UserController;
import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.AuthRequest;
import YOURSSU.blog.dto.request.UserRequest;
import YOURSSU.blog.dto.response.AuthResponse;
import YOURSSU.blog.dto.response.UserResponse;
import YOURSSU.blog.global.auth.filter.JwtAuthenticationFilter;
import YOURSSU.blog.global.auth.handler.resolver.AuthUserArgumentResolver;
import YOURSSU.blog.global.auth.provider.JwtTokenProvider;
import YOURSSU.blog.global.exception.GlobalErrorCode;
import YOURSSU.blog.global.exception.GlobalException;
import YOURSSU.blog.service.auth.AuthService;
import YOURSSU.blog.service.user.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @MockBean private UserService userService;
    @MockBean private AuthService authService;
    @MockBean private AuthUserArgumentResolver authUserArgumentResolver;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String LOCAL_DATE_TIME_PATTERN =
            "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{6}";

    private String token;

    @BeforeEach
    public void setup() {
        // 테스트 토큰 생성
        AuthRequest.LoginRequest loginRequest =
                new AuthRequest.LoginRequest("email@urssu.com", "password");
        AuthResponse.LoginResponse loginResponse =
                new AuthResponse.LoginResponse("email@urssu.com", "access-token", "refresh-token");
        Mockito.when(authService.login(any())).thenReturn(loginResponse);
        token = authService.login(loginRequest).getAccessToken();
        // mock 설정
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .alwaysDo(print())
                        .build();
    }

    @Test
    public void 회원가입() throws Exception {
        // given
        UserRequest.UserSignUpRequest request =
                new UserRequest.UserSignUpRequest("email@urssu.com", "password", "username");
        UserResponse.UserSignUpResponse response =
                UserResponse.UserSignUpResponse.builder()
                        .email("email@urssu.com")
                        .username("username")
                        .build();

        // when
        Mockito.when(userService.signUp(any())).thenReturn(response);

        // then
        mockMvc.perform(
                        post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void 회원가입_중복회원() throws Exception {
        // given
        UserRequest.UserSignUpRequest request =
                new UserRequest.UserSignUpRequest("email@urssu.com", "password", "username");

        // when
        Mockito.when(userService.signUp(any()))
                .thenThrow(new GlobalException(GlobalErrorCode.USER_ALREADY_EXISTS));

        // then
        mockMvc.perform(
                        post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 사용자입니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/users/signup"));
    }

    @Test
    public void 회원탈퇴() throws Exception {
        // given

        // when
        Mockito.doNothing().when(userService).withdraw(any(User.class));

        // then
        mockMvc.perform(
                        delete("/api/users/withdraw")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void 회원탈퇴_사용자없음() throws Exception {
        // given

        // when
        Mockito.doThrow(new GlobalException(GlobalErrorCode.USER_NOT_FOUND))
                .when(userService)
                .withdraw(any(User.class));

        // then
        mockMvc.perform(
                        delete("/api/users/withdraw")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 사용자를 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/users/withdraw"));
    }
}
