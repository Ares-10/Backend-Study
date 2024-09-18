package YOURSSU.assignment;

import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import YOURSSU.assignment.controller.UserController;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.AuthRequest;
import YOURSSU.assignment.dto.request.UserRequest;
import YOURSSU.assignment.dto.response.AuthResponse;
import YOURSSU.assignment.dto.response.UserResponse;
import YOURSSU.assignment.global.auth.filter.JwtAuthenticationFilter;
import YOURSSU.assignment.global.auth.handler.resolver.AuthUserArgumentResolver;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.service.auth.AuthService;
import YOURSSU.assignment.service.user.UserService;

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
            "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?";

    @BeforeEach
    public void setup() {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .alwaysDo(print())
                        .build();
    }

    @Test
    public void 회원가입() throws Exception {

        // given
        UserRequest.UserSignUpRequest request = new UserRequest.UserSignUpRequest();
        request.setEmail("email@urssu.com");
        request.setPassword("password");
        request.setUsername("username");

        // when
        UserResponse.UserSignUpResponse response =
                UserResponse.UserSignUpResponse.builder()
                        .email("email@urssu.com")
                        .username("username")
                        .build();

        Mockito.when(userService.signUp(any())).thenReturn(response);

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void 회원가입_중복회원() throws Exception {

        // given
        UserRequest.UserSignUpRequest request = new UserRequest.UserSignUpRequest();
        request.setEmail("email@urssu.com");
        request.setPassword("password");
        request.setUsername("username");

        // when
        Mockito.when(userService.signUp(any()))
                .thenThrow(new GlobalException(GlobalErrorCode.USER_ALREADY_EXISTS));

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
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
        User user =
                User.builder()
                        .email("email@urssu.com")
                        .username("username")
                        .password("password")
                        .build();

        AuthRequest.LoginRequest loginRequest =
                new AuthRequest.LoginRequest("email@urssu.com", "password");

        AuthResponse.LoginResponse loginResponse =
                new AuthResponse.LoginResponse("email@urssu.com", "access-token", "refresh-token");

        Mockito.when(authService.login(any())).thenReturn(loginResponse);
        String token = authService.login(loginRequest).getAccessToken();

        // when
        Mockito.doNothing().when(userService).withdraw(any(User.class));

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/users/withdraw")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
