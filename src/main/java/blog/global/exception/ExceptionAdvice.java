package blog.global.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import blog.global.exception.response.ErrorResponse;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // GlobalException 예외 처리 핸들러
    @ExceptionHandler(value = {GlobalException.class})
    protected ResponseEntity<ErrorResponse> handleGlobalException(
            GlobalException e, HttpServletRequest request) {
        ErrorResponse errorResponse =
                new ErrorResponse(e.getGlobalErrorCode(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, e.getGlobalErrorCode().getHttpStatus());
    }

    // DTO 수준 예외 처리 핸들러
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String requestURI = request.getDescription(false).substring(4);
        String message = e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST.name(), message, requestURI);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
