package YOURSSU.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import YOURSSU.blog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
