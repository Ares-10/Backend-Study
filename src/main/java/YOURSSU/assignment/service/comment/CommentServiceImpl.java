package YOURSSU.assignment.service.comment;

import jakarta.transaction.Transactional;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    // 유저 권한 검증 메서드
    private void checkUserAccess(User user, Comment comment) {
        if (!user.getId().equals(comment.getUser().getId()))
            throw new GlobalException(GlobalErrorCode.COMMENT_ACCESS_DENIED);
    }

    // 게시글과 댓글이 일치하는지 확인하는 메서드
    private void checkArticleCommentMatch(Long articleId, Comment comment) {
        if (!comment.getArticle().getId().equals(articleId))
            throw new GlobalException(GlobalErrorCode.COMMENT_NOT_MATCH);
    }

    private Comment getComment(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND));
    }

    @Override
    @Transactional
    public CommentCreateResponse createComment(
            Long articleId, CommentCreateRequest request, User user) {
        Article article = articleService.getArticle(articleId);
        Comment comment = CommentConverter.toComment(request, article, user);
        commentRepository.save(comment);
        return CommentConverter.toCommentCreateResponse(comment, user.getEmail());
    }

    @Override
    @Transactional
    public CommentUpdateResponse updateComment(
            Long commentId, Long articleId, CommentUpdateRequest request, User user) {
        Comment comment = getComment(commentId);
        checkArticleCommentMatch(articleId, comment);
        checkUserAccess(user, comment);
        comment.update(request.getContent());
        return CommentConverter.toCommentUpdateResponse(comment, user.getEmail());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long articleId, User user) {
        Comment comment = getComment(commentId);
        checkArticleCommentMatch(articleId, comment);
        checkUserAccess(user, comment);
        commentRepository.deleteById(commentId);
    }
}
