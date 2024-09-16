package YOURSSU.assignment.service.comment;

import YOURSSU.assignment.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.assignment.dto.request.CommentRequest.CommentDeleteRequest;
import YOURSSU.assignment.dto.request.CommentRequest.CommentUpdateRequest;
import YOURSSU.assignment.dto.response.CommentResponse.CommentCreateResponse;
import YOURSSU.assignment.dto.response.CommentResponse.CommentUpdateResponse;

public interface CommentService {
    CommentCreateResponse createComment(Long articleId, CommentCreateRequest request);

    CommentUpdateResponse updateComment(Long id, CommentUpdateRequest request);

    void deleteComment(Long id, CommentDeleteRequest request);
}
