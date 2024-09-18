package YOURSSU.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import YOURSSU.assignment.controller.UserController;
import YOURSSU.assignment.global.auth.filter.JwtAuthenticationFilter;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import YOURSSU.assignment.service.auth.AuthService;
import YOURSSU.assignment.service.comment.CommentService;

@WebMvcTest(UserController.class)
public class CommentControllerTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @MockBean private CommentService commentService;
    @MockBean private AuthService authService;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;
}
