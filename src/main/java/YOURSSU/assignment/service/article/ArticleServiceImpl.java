package YOURSSU.assignment.service.article;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.ArticleConverter;
import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleCreateResponse;
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
}
