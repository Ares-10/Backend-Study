package YOURSSU.assignment.converter;

import YOURSSU.assignment.domain.Article;
import YOURSSU.assignment.domain.Comment;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.CommentRequest.CommentCreateRequest;
import YOURSSU.assignment.dto.response.CommentResponse.CommentCreateResponse;

public class CommentConverter {
    public static Comment toComment(CommentCreateRequest request, Article article, User user) {
        Comment comment =
                Comment.builder().content(request.getContent()).article(article).user(user).build();
        article.getComments().add(comment);
        user.getComments().add(comment);
        return comment;
    }

    public static CommentCreateResponse toCommentCreateResponse(Comment comment, String email) {
        return CommentCreateResponse.builder()
                .commentId(comment.getId())
                .email(email)
                .content(comment.getContent())
                .build();
    }
}
