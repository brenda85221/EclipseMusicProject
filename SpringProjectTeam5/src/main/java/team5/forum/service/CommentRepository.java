package team5.forum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team5.forum.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticle_ArticleId(Integer articleId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.article.articleId = :articleId")
    long countByArticleId(int articleId);
}
