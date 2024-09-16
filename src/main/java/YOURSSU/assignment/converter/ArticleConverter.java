package YOURSSU.assignment.converter;

import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleCreateResponse;

public class ArticleConverter {
    public static Article toArticle(ArticleCreateRequest request, User user) {
        Article article =
                Article.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .user(user)
                        .build();
        user.getArticles().add(article);
        return article;
    }

    public static ArticleCreateResponse toArticleCreateResponse(Article article, String email) {
        return ArticleCreateResponse.builder()
                .articleId(article.getId())
                .email(email)
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
