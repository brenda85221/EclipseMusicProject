package team5.forum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.forum.model.ArticleType;
import team5.forum.model.ForumType;


public interface ArticleTypeRepository extends JpaRepository<ArticleType, Integer> {
    ArticleType findByArticleTypeName(String articleTypeName);
}
