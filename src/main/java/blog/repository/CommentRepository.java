package blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
