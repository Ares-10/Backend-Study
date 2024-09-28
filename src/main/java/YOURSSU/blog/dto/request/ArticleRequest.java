package YOURSSU.blog.dto.request;

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
        @NotBlank private String title;
        @NotBlank private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleUpdateRequest {
        @NotBlank private String title;
        @NotBlank private String content;
    }
}
