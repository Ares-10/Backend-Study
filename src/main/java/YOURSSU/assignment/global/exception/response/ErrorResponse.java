package YOURSSU.assignment.global.exception.response;

import java.time.LocalDateTime;

import YOURSSU.assignment.global.exception.GlobalErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private String time;
    private String status;
    private String message;
    private String requestURI;

    public ErrorResponse(GlobalErrorCode e, String requestURI) {
        this.time = LocalDateTime.now().toString();
        this.status = e.getHttpStatus().name();
        this.message = e.getMessage();
        this.requestURI = requestURI;
    }

    public ErrorResponse(String status, String message, String requestURI) {
        this.time = LocalDateTime.now().toString();
        this.status = status;
        this.message = message;
        this.requestURI = requestURI;
    }
}
