package YOURSSU.assignment.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import YOURSSU.assignment.dto.request.CommentRequest.*;
import YOURSSU.assignment.dto.response.CommentResponse.*;
import YOURSSU.assignment.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 수정하기")
    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request) {
        CommentUpdateResponse response = commentService.updateComment(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
