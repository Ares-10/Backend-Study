package YOURSSU.assignment.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러, 관리자에게 문의 바립니다"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 잘못되었습니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없는 요청입니다"),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다"),

    // 유저 관련
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
