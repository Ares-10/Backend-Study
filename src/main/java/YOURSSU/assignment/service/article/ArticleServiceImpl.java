package YOURSSU.assignment.service.article;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.ArticleConverter;
import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.ArticleRequest.*;
import YOURSSU.assignment.dto.response.ArticleResponse.*;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    // 유저 권한 검증 메서드
    private void checkUserAccess(User user, Article article) {
        if (!user.getId().equals(article.getUser().getId()))
            throw new GlobalException(GlobalErrorCode.ARTICLE_ACCESS_DENIED);
    }

    public Article getArticle(Long id) {
        return articleRepository
                .findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.ARTICLE_NOT_FOUND));
    }

    @Override
    @Transactional
    public ArticleCreateResponse createArticle(ArticleCreateRequest request, User user) {
        Article article = ArticleConverter.toArticle(request, user);
        articleRepository.save(article);
        return ArticleConverter.toArticleCreateResponse(article, user.getEmail());
    }

    @Override
    @Transactional
    public ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request, User user) {
        Article article = getArticle(id);
        checkUserAccess(user, article);
        article.update(request.getTitle(), request.getContent());
        articleRepository.save(article);
        return ArticleConverter.toArticleUpdateResponse(article, user.getEmail());
    }

    @Override
    @Transactional
    public void deleteArticle(Long id, User user) {
        Article article = getArticle(id);
        checkUserAccess(user, article);
        articleRepository.deleteById(id);
    }
}
