package YOURSSU.assignment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import YOURSSU.assignment.controller.ArticleController;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.ArticleRequest;
import YOURSSU.assignment.dto.request.AuthRequest;
import YOURSSU.assignment.dto.response.ArticleResponse;
import YOURSSU.assignment.dto.response.AuthResponse;
import YOURSSU.assignment.global.auth.filter.JwtAuthenticationFilter;
import YOURSSU.assignment.global.auth.handler.resolver.AuthUserArgumentResolver;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.service.article.ArticleService;
import YOURSSU.assignment.service.auth.AuthService;

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @MockBean private ArticleService articleService;
    @MockBean private AuthService authService;
    @MockBean private AuthUserArgumentResolver authUserArgumentResolver;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String LOCAL_DATE_TIME_PATTERN =
            "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{6}";

    private User user;

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
        user =
                User.builder()
                        .email("email@urssu.com")
                        .username("username")
                        .password("password")
                        .build();
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .alwaysDo(print())
                        .build();
    }

    @Test
    public void 게시글작성하기() throws Exception {
        // given
        ArticleRequest.ArticleCreateRequest request =
                new ArticleRequest.ArticleCreateRequest("title", "content");
        ArticleResponse.ArticleCreateResponse response =
                ArticleResponse.ArticleCreateResponse.builder()
                        .articleId(1L)
                        .email(user.getEmail())
                        .title("title")
                        .content("content")
                        .build();

        // when
        when(articleService.createArticle(
                        any(ArticleRequest.ArticleCreateRequest.class), any(User.class)))
                .thenReturn(response);

        // then
        mockMvc.perform(
                        post("/api/articles/")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(1L))
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    public void 게시글업데이트() throws Exception {
        // given
        Long articleId = 1L;
        ArticleRequest.ArticleUpdateRequest request =
                new ArticleRequest.ArticleUpdateRequest("new title", "new content");
        ArticleResponse.ArticleUpdateResponse response =
                ArticleResponse.ArticleUpdateResponse.builder()
                        .articleId(articleId)
                        .email(user.getEmail())
                        .title("new title")
                        .content("new content")
                        .build();

        // when
        when(articleService.updateArticle(
                        eq(articleId),
                        any(ArticleRequest.ArticleUpdateRequest.class),
                        any(User.class)))
                .thenReturn(response);

        // then
        mockMvc.perform(
                        put("/api/articles/{id}", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(articleId))
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.content").value("new content"));
    }

    @Test
    public void 게시글업데이트_게시글없음() throws Exception {
        // given
        Long articleId = 1L;
        ArticleRequest.ArticleUpdateRequest request =
                new ArticleRequest.ArticleUpdateRequest("new title", "new content");

        // when
        when(articleService.updateArticle(
                        eq(articleId),
                        any(ArticleRequest.ArticleUpdateRequest.class),
                        any(User.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND));

        // then
        mockMvc.perform(
                        put("/api/articles/{id}", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1"));
    }

    @Test
    public void 게시글업데이트_권한없음() throws Exception {
        // given
        Long articleId = 1L;
        ArticleRequest.ArticleUpdateRequest request =
                new ArticleRequest.ArticleUpdateRequest("new title", "new content");

        // when
        when(articleService.updateArticle(
                        eq(articleId),
                        any(ArticleRequest.ArticleUpdateRequest.class),
                        any(User.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.ARTICLE_ACCESS_DENIED));

        // then
        mockMvc.perform(
                        put("/api/articles/{id}", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("해당 게시글에 대한 권한이 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1"));
    }

    @Test
    public void 게시글삭제하기() throws Exception {
        // given
        Long articleId = 1L;

        // when
        doNothing().when(articleService).deleteArticle(articleId, user);

        // then
        mockMvc.perform(
                        delete("/api/articles/{id}", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void 게시글삭제하기_게시글없음() throws Exception {
        // given
        Long articleId = 1L;

        // when
        doThrow(new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND))
                .when(articleService)
                .deleteArticle(any(), any());

        // then
        mockMvc.perform(
                        delete("/api/articles/{id}", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1"));
    }

    @Test
    public void 게시글삭제하기_권한없음() throws Exception {
        // given
        Long articleId = 1L;

        // when
        doThrow(new GlobalException(GlobalErrorCode.ARTICLE_ACCESS_DENIED))
                .when(articleService)
                .deleteArticle(any(), any());

        // then
        mockMvc.perform(
                        delete("/api/articles/{id}", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("해당 게시글에 대한 권한이 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1"));
    }
}
