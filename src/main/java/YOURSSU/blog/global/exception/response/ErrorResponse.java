package YOURSSU.blog.global.exception.response;

import java.time.LocalDateTime;

import YOURSSU.blog.global.exception.GlobalErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private LocalDateTime time;
    private String status;
    private String message;
    private String requestURI;

    public ErrorResponse(GlobalErrorCode e, String requestURI) {
        this.time = LocalDateTime.now();
        this.status = e.getHttpStatus().name();
        this.message = e.getMessage();
        this.requestURI = requestURI;
    }

    public ErrorResponse(String status, String message, String requestURI) {
        this.time = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.requestURI = requestURI;
    }
}
