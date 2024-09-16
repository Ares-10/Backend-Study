package YOURSSU.assignment.service.article;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.ArticleConverter;
import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.assignment.dto.request.ArticleRequest.ArticleUpdateRequest;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleCreateResponse;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleUpdateResponse;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.repository.ArticleRepository;
import YOURSSU.assignment.service.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Override
    public ArticleCreateResponse createArticle(ArticleCreateRequest request) {
        User user = userService.getUser(request.getEmail(), request.getPassword());
        Article article = ArticleConverter.toArticle(request, user);
        articleRepository.save(article);
        return ArticleConverter.toArticleCreateResponse(article, user.getEmail());
    }

    @Override
    public ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request) {
        // 게시글이 없는 경우
        Article article =
                articleRepository
                        .findById(id)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND));
        User user = userService.getUser(request.getEmail(), request.getPassword());
        // 유저가 게시글에 대한 권한이 없는 경우
        if (!user.getId().equals(article.getUser().getId()))
            throw new GlobalException(GlobalErrorCode.ARTICLE_ACCESS_DENIED);
        article.update(request.getTitle(), request.getContent());
        articleRepository.save(article);
        return ArticleConverter.toArticleUpdateResponse(article, user.getEmail());
    }
}
