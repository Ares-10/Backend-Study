package YOURSSU.assignment.service.article;

import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.ArticleRequest.*;
import YOURSSU.assignment.dto.response.ArticleResponse.*;

public interface ArticleService {

    Article getArticle(Long id);

    ArticleCreateResponse createArticle(ArticleCreateRequest request, User user);

    ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request, User user);

    void deleteArticle(Long id, User user);
}
