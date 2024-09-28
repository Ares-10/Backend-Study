package YOURSSU.blog.service.article;

import YOURSSU.blog.domain.Article;
import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.ArticleRequest.*;
import YOURSSU.blog.dto.response.ArticleResponse.*;

public interface ArticleService {

    Article getArticle(Long id);

    ArticleCreateResponse createArticle(ArticleCreateRequest request, User user);

    ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request, User user);

    void deleteArticle(Long id, User user);
}
