package YOURSSU.assignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ArticleResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleCreateResponse {
        private Long articleId;
        private String email;
        private String title;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleUpdateResponse {
        private Long articleId;
        private String email;
        private String title;
        private String content;
    }
}
