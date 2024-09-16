package YOURSSU.assignment.service.comment;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.CommentConverter;
import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.Comment;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.assignment.dto.response.CommentResponse.CommentCreateResponse;
import YOURSSU.assignment.repository.CommentRepository;
import YOURSSU.assignment.service.article.ArticleService;
import YOURSSU.assignment.service.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;

    @Override
    public CommentCreateResponse createComment(Long articleId, CommentCreateRequest request) {
        User user = userService.getUser(request.getEmail(), request.getPassword());
        Article article = articleService.getArticle(articleId);
        Comment comment = CommentConverter.toComment(request, article, user);
        commentRepository.save(comment);
        return CommentConverter.toCommentCreateResponse(comment, user.getEmail());
    }
}
