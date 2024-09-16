package YOURSSU.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import YOURSSU.assignment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
