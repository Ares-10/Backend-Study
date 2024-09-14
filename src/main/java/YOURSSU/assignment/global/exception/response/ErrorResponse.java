package YOURSSU.assignment.global.exception.response;

import java.time.LocalDateTime;

import YOURSSU.assignment.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private LocalDateTime time;
    private String status;
    private String message;
    private String requestURI;

    public ErrorResponse(GlobalException e, String requestURI) {
        this.time = LocalDateTime.now();
        this.status = e.getGlobalErrorCode().getHttpStatus().name();
        this.message = e.getGlobalErrorCode().getMessage();
        this.requestURI = requestURI;
    }
}
