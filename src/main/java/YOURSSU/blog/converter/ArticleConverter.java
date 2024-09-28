package YOURSSU.blog.converter;

import YOURSSU.blog.domain.Article;
import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.blog.dto.response.ArticleResponse.*;

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

    public static ArticleUpdateResponse toArticleUpdateResponse(Article article, String email) {
        return ArticleUpdateResponse.builder()
                .articleId(article.getId())
                .email(email)
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
