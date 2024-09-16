package YOURSSU.assignment.service.comment;

import YOURSSU.assignment.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.assignment.dto.response.CommentResponse.CommentCreateResponse;

public interface CommentService {
    CommentCreateResponse createComment(Long articleId, CommentCreateRequest request);
}
