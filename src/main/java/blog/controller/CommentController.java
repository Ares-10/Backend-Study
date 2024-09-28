package blog.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.domain.User;
import blog.dto.request.CommentRequest.*;
import blog.dto.response.CommentResponse.*;
import blog.global.auth.handler.annotation.AuthUser;
import blog.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{articleId}/comments")
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성하기")
    @PostMapping("/")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentCreateRequest request,
            @Parameter(name = "user", hidden = true) @AuthUser User user) {
        CommentCreateResponse response = commentService.createComment(articleId, request, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "댓글 수정하기")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable Long commentId,
            @PathVariable Long articleId,
            @Valid @RequestBody CommentUpdateRequest request,
            @Parameter(name = "user", hidden = true) @AuthUser User user) {
        CommentUpdateResponse response =
                commentService.updateComment(commentId, articleId, request, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제하기")
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long commentId,
            @PathVariable Long articleId,
            @Parameter(name = "user", hidden = true) @AuthUser User user) {
        commentService.deleteComment(commentId, articleId, user);
    }
}
