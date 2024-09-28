package YOURSSU.blog.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.ArticleRequest.ArticleCreateRequest;
import YOURSSU.blog.dto.request.ArticleRequest.ArticleUpdateRequest;
import YOURSSU.blog.dto.response.ArticleResponse.ArticleCreateResponse;
import YOURSSU.blog.dto.response.ArticleResponse.ArticleUpdateResponse;
import YOURSSU.blog.global.auth.handler.annotation.AuthUser;
import YOURSSU.blog.service.article.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            @Valid @RequestBody ArticleCreateRequest request,
            @Parameter(name = "user", hidden = true) @AuthUser User user) {
        ArticleCreateResponse response = articleService.createArticle(request, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "게시글 수정하기")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleUpdateResponse> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleUpdateRequest request,
            @Parameter(name = "user", hidden = true) @AuthUser User user) {
        ArticleUpdateResponse response = articleService.updateArticle(id, request, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "게시글 삭제하기")
    @DeleteMapping("/{id}")
    public void deleteArticle(
            @PathVariable Long id, @Parameter(name = "user", hidden = true) @AuthUser User user) {
        articleService.deleteArticle(id, user);
    }
}
