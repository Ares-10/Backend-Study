package blog.global.auth.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import blog.global.exception.GlobalErrorCode;
import blog.global.exception.response.ErrorResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper mapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);

        ErrorResponse errorResponse =
                new ErrorResponse(GlobalErrorCode._FORBIDDEN, request.getRequestURI());

        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
