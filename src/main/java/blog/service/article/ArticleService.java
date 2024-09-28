package blog.service.article;

import blog.domain.Article;
import blog.domain.User;
import blog.dto.request.ArticleRequest.*;
import blog.dto.response.ArticleResponse.*;

public interface ArticleService {

    Article getArticle(Long id);

    ArticleCreateResponse createArticle(ArticleCreateRequest request, User user);

    ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request, User user);

    void deleteArticle(Long id, User user);
}
