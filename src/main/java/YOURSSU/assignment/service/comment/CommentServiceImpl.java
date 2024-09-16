package YOURSSU.assignment.service.comment;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.CommentConverter;
import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.Comment;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.assignment.dto.request.CommentRequest.CommentUpdateRequest;
import YOURSSU.assignment.dto.response.CommentResponse.CommentCreateResponse;
import YOURSSU.assignment.dto.response.CommentResponse.CommentUpdateResponse;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
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

    // 유저 권한 검증 메서드
    private void checkUserAccess(User user, Comment comment) {
        if (!user.getId().equals(comment.getUser().getId()))
            throw new GlobalException(GlobalErrorCode.COMMENT_ACCESS_DENIED);
    }

    private Comment getComment(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND));
    }

    @Override
    public CommentCreateResponse createComment(Long articleId, CommentCreateRequest request) {
        User user = userService.getUser(request.getEmail(), request.getPassword());
        Article article = articleService.getArticle(articleId);
        Comment comment = CommentConverter.toComment(request, article, user);
        commentRepository.save(comment);
        return CommentConverter.toCommentCreateResponse(comment, user.getEmail());
    }

    @Override
    public CommentUpdateResponse updateComment(Long id, CommentUpdateRequest request) {
        User user = userService.getUser(request.getEmail(), request.getPassword());
        Comment comment = getComment(id);
        checkUserAccess(user, comment);
        comment.update(request.getContent());
        commentRepository.save(comment);
        return CommentConverter.toCommentUpdateResponse(comment, user.getEmail());
    }
}
