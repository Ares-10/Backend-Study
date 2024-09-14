package YOURSSU.assignment.global.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import YOURSSU.assignment.global.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // GlobalException 예외 처리 핸들러
    @ExceptionHandler(value = {GlobalException.class})
    protected ResponseEntity<ErrorResponse> handleGlobalException(
            GlobalException e, HttpServletRequest request) {
        log.error("{}: {}", e.getGlobalErrorCode(), e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, e.getGlobalErrorCode().getHttpStatus());
    }
}
