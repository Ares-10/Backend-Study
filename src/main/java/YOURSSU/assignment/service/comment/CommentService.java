package YOURSSU.assignment.service.comment;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.assignment.dto.request.CommentRequest.CommentUpdateRequest;
import YOURSSU.assignment.dto.response.CommentResponse.CommentCreateResponse;
import YOURSSU.assignment.dto.response.CommentResponse.CommentUpdateResponse;

public interface CommentService {
    CommentCreateResponse createComment(Long articleId, CommentCreateRequest request, User user);

    CommentUpdateResponse updateComment(Long id, CommentUpdateRequest request, User user);

    void deleteComment(Long id, User user);
}
