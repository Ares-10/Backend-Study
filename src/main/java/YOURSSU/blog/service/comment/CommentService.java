package YOURSSU.blog.service.comment;

import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.blog.dto.request.CommentRequest.CommentUpdateRequest;
import YOURSSU.blog.dto.response.CommentResponse.CommentCreateResponse;
import YOURSSU.blog.dto.response.CommentResponse.CommentUpdateResponse;

public interface CommentService {
    CommentCreateResponse createComment(Long articleId, CommentCreateRequest request, User user);

    CommentUpdateResponse updateComment(
            Long commentId, Long articleId, CommentUpdateRequest request, User user);

    void deleteComment(Long commentId, Long articleId, User user);
}
