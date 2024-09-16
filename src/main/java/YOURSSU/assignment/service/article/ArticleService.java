package YOURSSU.assignment.service.article;

import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.dto.request.ArticleRequest.*;
import YOURSSU.assignment.dto.response.ArticleResponse.*;

public interface ArticleService {

    Article getArticle(Long id);

    ArticleCreateResponse createArticle(ArticleCreateRequest request);

    ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request);

    void deleteArticle(Long id, ArticleDeleteRequest request);
}
