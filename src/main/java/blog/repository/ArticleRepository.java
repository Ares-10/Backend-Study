package blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {}
