package team5.forum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.forum.model.ForumType;


public interface ForumTypeRepository extends JpaRepository<ForumType, Integer> {
    ForumType findByForumTypeName(String forumTypeName);
}
