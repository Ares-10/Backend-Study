package YOURSSU.assignment.service.article;

import org.springframework.stereotype.Service;

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

        Article article =
                Article.builder().title(request.getTitle()).content(request.getContent()).build();
        article.setUser(user);
        articleRepository.save(article);

        return ArticleCreateResponse.builder()
                .articleId(article.getId())
                .email(user.getEmail())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
