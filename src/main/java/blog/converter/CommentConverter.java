package blog.converter;

import blog.domain.Article;
import blog.domain.Comment;
import blog.domain.User;
import blog.dto.request.CommentRequest.*;
import blog.dto.response.CommentResponse.*;

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

    public static CommentUpdateResponse toCommentUpdateResponse(Comment comment, String email) {
        return CommentUpdateResponse.builder()
                .commentId(comment.getId())
                .email(email)
                .content(comment.getContent())
                .build();
    }
}
