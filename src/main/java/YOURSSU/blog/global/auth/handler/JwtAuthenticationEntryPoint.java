package YOURSSU.blog.global.auth.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import YOURSSU.blog.global.exception.GlobalErrorCode;
import YOURSSU.blog.global.exception.response.ErrorResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);

        ErrorResponse errorResponse =
                new ErrorResponse(GlobalErrorCode._FORBIDDEN, request.getRequestURI());

        mapper.writeValue(response.getOutputStream(), errorResponse); // json 변환
    }
}
