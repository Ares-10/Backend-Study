package YOURSSU.assignment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CommentRequest {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentCreateRequest {
        @NotBlank
        @Email(message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank private String password;
        @NotBlank private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentUpdateRequest {
        @NotBlank
        @Email(message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank private String password;
        @NotBlank private String content;
    }
}
