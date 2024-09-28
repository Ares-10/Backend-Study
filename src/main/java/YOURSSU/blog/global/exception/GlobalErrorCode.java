package YOURSSU.blog.global.exception;

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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다"),

    // 게시글 관련
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다"),
    ARTICLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 게시글에 대한 권한이 없습니다"),

    // 댓글 관련
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다"),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 댓글에 대한 권한이 없습니다"),
    COMMENT_NOT_MATCH(HttpStatus.FORBIDDEN, "게시물과 댓글이 일치하지 않습니다"),

    // 이메일과 비밀번호가 일치하지 않는 경우
    EMAIL_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "이메일에 대한 비밀번호가 일치하지 않습니다"),

    // 인증 관련
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    AUTH_INVALID_TOKEN(HttpStatus.NOT_FOUND, "토큰이 유효하지 않습니다"),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰입니다");

    private final HttpStatus httpStatus;
    private final String message;
}
