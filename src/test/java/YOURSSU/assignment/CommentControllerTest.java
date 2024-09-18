package YOURSSU.assignment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import YOURSSU.assignment.controller.CommentController;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.AuthRequest;
import YOURSSU.assignment.dto.request.CommentRequest;
import YOURSSU.assignment.dto.response.AuthResponse;
import YOURSSU.assignment.dto.response.CommentResponse;
import YOURSSU.assignment.global.auth.filter.JwtAuthenticationFilter;
import YOURSSU.assignment.global.auth.handler.resolver.AuthUserArgumentResolver;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.service.auth.AuthService;
import YOURSSU.assignment.service.comment.CommentService;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @MockBean private CommentService commentService;
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
        AuthRequest.LoginRequest loginRequest =
                new AuthRequest.LoginRequest("email@urssu.com", "password");
        AuthResponse.LoginResponse loginResponse =
                new AuthResponse.LoginResponse("email@urssu.com", "access-token", "refresh-token");
        when(authService.login(any())).thenReturn(loginResponse);
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
    public void 댓글작성하기() throws Exception {
        // given
        Long articleId = 1L;
        CommentRequest.CommentCreateRequest request =
                new CommentRequest.CommentCreateRequest("content");
        CommentResponse.CommentCreateResponse response =
                CommentResponse.CommentCreateResponse.builder()
                        .commentId(1L)
                        .email(user.getEmail())
                        .content("content")
                        .build();

        // when
        when(commentService.createComment(
                        eq(articleId),
                        any(CommentRequest.CommentCreateRequest.class),
                        any(User.class)))
                .thenReturn(response);

        // then
        mockMvc.perform(
                        post("/api/articles/{articleId}/comments/", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    public void 댓글작성하기_게시글없음() throws Exception {
        // given
        Long articleId = 1L;
        CommentRequest.CommentCreateRequest request =
                new CommentRequest.CommentCreateRequest("content");

        // when
        when(commentService.createComment(
                        eq(articleId),
                        any(CommentRequest.CommentCreateRequest.class),
                        any(User.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND));

        // then
        mockMvc.perform(
                        post("/api/articles/{articleId}/comments/", articleId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/"));
    }

    @Test
    public void 댓글수정하기() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;
        CommentRequest.CommentUpdateRequest request =
                new CommentRequest.CommentUpdateRequest("new content");
        CommentResponse.CommentUpdateResponse response =
                CommentResponse.CommentUpdateResponse.builder()
                        .commentId(commentId)
                        .email(user.getEmail())
                        .content("new content")
                        .build();

        // when
        when(commentService.updateComment(
                        eq(commentId),
                        eq(articleId),
                        any(CommentRequest.CommentUpdateRequest.class),
                        any(User.class)))
                .thenReturn(response);

        // then
        mockMvc.perform(
                        put("/api/articles/{articleId}/comments/{commentId}", articleId, commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(commentId))
                .andExpect(jsonPath("$.email").value("email@urssu.com"))
                .andExpect(jsonPath("$.content").value("new content"));
    }

    @Test
    public void 댓글수정하기_게시글없음() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;
        CommentRequest.CommentUpdateRequest request =
                new CommentRequest.CommentUpdateRequest("new content");

        // when
        when(commentService.updateComment(
                        eq(commentId),
                        eq(articleId),
                        any(CommentRequest.CommentUpdateRequest.class),
                        any(User.class)))
                .thenThrow(new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND));

        // then
        mockMvc.perform(
                        put("/api/articles/{articleId}/comments/{commentId}", articleId, commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글수정하기_권한없음() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;
        CommentRequest.CommentUpdateRequest request =
                new CommentRequest.CommentUpdateRequest("new content");

        // when
        doThrow(new GlobalException(GlobalErrorCode.COMMENT_ACCESS_DENIED))
                .when(commentService)
                .updateComment(any(), any(), any(), any());

        // then
        mockMvc.perform(
                        put("/api/articles/{articleId}/comments/{commentId}", articleId, commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("해당 댓글에 대한 권한이 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글수정하기_댓글없음() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;
        CommentRequest.CommentUpdateRequest request =
                new CommentRequest.CommentUpdateRequest("new content");

        // when
        doThrow(new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND))
                .when(commentService)
                .updateComment(any(), any(), any(), any());

        // then
        mockMvc.perform(
                        put("/api/articles/{articleId}/comments/{commentId}", articleId, commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 댓글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글수정하기_게시글과댓글불일치() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;
        CommentRequest.CommentUpdateRequest request =
                new CommentRequest.CommentUpdateRequest("new content");

        // when
        doThrow(new GlobalException(GlobalErrorCode.COMMENT_NOT_MATCH))
                .when(commentService)
                .updateComment(
                        eq(commentId),
                        eq(articleId),
                        any(CommentRequest.CommentUpdateRequest.class),
                        any(User.class));

        // then
        mockMvc.perform(
                        put("/api/articles/{articleId}/comments/{commentId}", articleId, commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("게시물과 댓글이 일치하지 않습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글삭제하기() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;

        // when
        doNothing()
                .when(commentService)
                .deleteComment(eq(commentId), eq(articleId), any(User.class));

        // then
        mockMvc.perform(
                        delete(
                                        "/api/articles/{articleId}/comments/{commentId}",
                                        articleId,
                                        commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void 댓글삭제하기_게시글없음() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;

        // when
        doThrow(new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND))
                .when(commentService)
                .deleteComment(eq(commentId), eq(articleId), any(User.class));

        // then
        mockMvc.perform(
                        delete(
                                        "/api/articles/{articleId}/comments/{commentId}",
                                        articleId,
                                        commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 게시글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글삭제하기_권한없음() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;

        // when
        doThrow(new GlobalException(GlobalErrorCode.COMMENT_ACCESS_DENIED))
                .when(commentService)
                .deleteComment(any(), any(), any());

        // then
        mockMvc.perform(
                        delete(
                                        "/api/articles/{articleId}/comments/{commentId}",
                                        articleId,
                                        commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("해당 댓글에 대한 권한이 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글삭제하기_댓글없음() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;

        // when
        doThrow(new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND))
                .when(commentService)
                .deleteComment(any(), any(), any());

        // then
        mockMvc.perform(
                        delete(
                                        "/api/articles/{articleId}/comments/{commentId}",
                                        articleId,
                                        commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("해당 댓글을 찾을 수 없습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }

    @Test
    public void 댓글삭제하기_게시글과댓글불일치() throws Exception {
        // given
        Long articleId = 1L;
        Long commentId = 1L;

        // when
        doThrow(new GlobalException(GlobalErrorCode.COMMENT_NOT_MATCH))
                .when(commentService)
                .deleteComment(eq(commentId), eq(articleId), any(User.class));

        // then
        mockMvc.perform(
                        delete(
                                        "/api/articles/{articleId}/comments/{commentId}",
                                        articleId,
                                        commentId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.time").value(Matchers.matchesPattern(LOCAL_DATE_TIME_PATTERN)))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("게시물과 댓글이 일치하지 않습니다"))
                .andExpect(jsonPath("$.requestURI").value("/api/articles/1/comments/1"));
    }
}
