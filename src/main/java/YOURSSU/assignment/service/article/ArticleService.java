package YOURSSU.assignment.service.article;

import YOURSSU.assignment.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleCreateResponse;

public interface ArticleService {
    ArticleCreateResponse createArticle(ArticleCreateRequest request);
}
