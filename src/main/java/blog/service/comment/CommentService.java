package blog.service.comment;

import blog.domain.User;
import blog.dto.request.CommentRequest.CommentCreateRequest;
import blog.dto.request.CommentRequest.CommentUpdateRequest;
import blog.dto.response.CommentResponse.CommentCreateResponse;
import blog.dto.response.CommentResponse.CommentUpdateResponse;

public interface CommentService {
    CommentCreateResponse createComment(Long articleId, CommentCreateRequest request, User user);

    CommentUpdateResponse updateComment(
            Long commentId, Long articleId, CommentUpdateRequest request, User user);

    void deleteComment(Long commentId, Long articleId, User user);
}
