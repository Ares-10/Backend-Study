package YOURSSU.assignment.service.article;

import YOURSSU.assignment.dto.request.ArticleRequest.*;
import YOURSSU.assignment.dto.response.ArticleResponse.*;

public interface ArticleService {
    ArticleCreateResponse createArticle(ArticleCreateRequest request);

    ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request);
}
