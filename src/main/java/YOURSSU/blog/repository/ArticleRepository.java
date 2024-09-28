package YOURSSU.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import YOURSSU.blog.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {}
