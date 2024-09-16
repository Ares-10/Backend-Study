package YOURSSU.assignment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ArticleRequest {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleCreateRequest {
        @NotBlank
        @Email(message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank private String password;
        @NotBlank private String title;
        @NotBlank private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleUpdateRequest {
        @NotBlank
        @Email(message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank private String password;
        @NotBlank private String title;
        @NotBlank private String content;
    }
}
