package YOURSSU.assignment.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import YOURSSU.assignment.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.assignment.dto.request.ArticleRequest.ArticleDeleteRequest;
import YOURSSU.assignment.dto.request.ArticleRequest.ArticleUpdateRequest;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleCreateResponse;
import YOURSSU.assignment.dto.response.ArticleResponse.ArticleUpdateResponse;
import YOURSSU.assignment.service.article.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@Tag(name = "Article", description = "게시글 관련 API")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "게시글 작성하기")
    @PostMapping("/")
    public ResponseEntity<ArticleCreateResponse> createArticle(
            @Valid @RequestBody ArticleCreateRequest request) {
        ArticleCreateResponse response = articleService.createArticle(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "게시글 수정하기")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleUpdateResponse> updateArticle(
            @PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest request) {
        ArticleUpdateResponse response = articleService.updateArticle(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제하기")
    @DeleteMapping("/{id}")
    public void deleteArticle(
            @PathVariable Long id, @Valid @RequestBody ArticleDeleteRequest request) {
        articleService.deleteArticle(id, request);
    }
}
