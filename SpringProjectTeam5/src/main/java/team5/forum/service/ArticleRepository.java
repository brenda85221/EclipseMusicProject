package team5.forum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team5.forum.model.Article;

import java.util.Date;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    List<Article> findByArticleIsDeleted(int articleIsDeleted);
    Page<Article> findByArticleIsDeleted(Pageable pageable, int articleIsDeleted);
    Page<Article> findByArticleIsDeletedAndForumType_ForumTypeNameNot(Pageable pageable, int articleIsDeleted, String forumTypeName);

    Page<Article> findByArticleIsDeletedAndForumType_ForumTypeIdAndForumType_ForumTypeNameNot
            (Pageable pageable, int articleIsDeleted, int forumTypeId, String forumTypeName );

    List<Article> findByArticleIsDeletedAndArticlePostTimeAfter(int isDeleted, Date postTime);

    List<Article> findByAcct(String acct);

    List<Article> findByForumType_ForumTypeName(String forumTypeName);

    // 搜尋
    @Query("SELECT a FROM Article a " +
            "WHERE (a.articleTitle LIKE %:search% OR " +
            "a.articleContent LIKE %:search% OR " +
            "a.acct LIKE %:search%) " +
            "AND a.articleIsDeleted = :articleIsDeleted " +
            "AND a.forumType.forumTypeName != '公告'")
    Page<Article> searchWithDeletedFlag(
            @Param("search") String search, Pageable pageable,
            @Param("articleIsDeleted") Integer articleIsDeleted);

    @Query("SELECT a FROM Article a " +
            "WHERE (a.articleTitle LIKE %:search% OR " +
            "a.articleContent LIKE %:search% OR " +
            "a.acct LIKE %:search%) AND a.articleIsDeleted = :articleIsDeleted " +
            "AND a.forumType.forumTypeId = :forumTypeId")
    Page<Article> searchWithForumType(
            @Param("search") String search,
            Pageable pageable,
            @Param("articleIsDeleted") Integer articleIsDeleted,
            @Param("forumTypeId") Integer forumTypeId
    );

    @Query("SELECT a FROM Article a " +
            "WHERE (a.articleTitle LIKE %:search% OR " +
            "a.articleContent LIKE %:search% OR " +
            "a.acct LIKE %:search%) " +
            "AND a.articleIsDeleted = :articleIsDeleted " +
            "AND (:forumTypeId IS NULL OR a.forumType.forumTypeId = :forumTypeId) " +
            "AND (a.forumType.forumTypeName != '公告')")
    Page<Article> searchWithDeletedFlagAndOptionalForumType(@Param("search") String search,
                                                            Pageable pageable,
                                                            @Param("articleIsDeleted") Integer articleIsDeleted,
                                                            @Param("forumTypeId") Integer forumTypeId);

    @Query("SELECT a FROM Article a " +
            "WHERE (:acct IS NULL OR a.acct = :acct) " +
            "AND (:forumTypeId IS NULL OR a.forumType.forumTypeId = :forumTypeId) " +
            "AND (:startTime IS NULL OR a.articlePostTime >= :startTime) " +
            "AND (:endTime IS NULL OR a.articlePostTime <= :endTime) " +
            "AND (:search IS NULL OR (a.articleTitle LIKE %:search% OR a.articleContent LIKE %:search%)) ")
    List<Article> findByAcctAndOptionalFilters(
            @Param("acct") String acct,
            @Param("forumTypeId") Integer forumTypeId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("search") String search,
            Sort sort);














}
