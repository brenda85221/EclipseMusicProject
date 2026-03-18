package team5.forum.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team5.forum.model.ArticleViewCount;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ArticleViewCountRepository extends JpaRepository<ArticleViewCount, Integer> {

    List<ArticleViewCount> findByArticle_ArticleId(int articleId);

    // 根據 articleId 查詢點擊數量
    @Query("SELECT COUNT(avc) FROM ArticleViewCount avc WHERE avc.article.articleId = :articleId")
    long countByArticleId(int articleId);

    // 查詢某一天或某一段時間內每篇文章的點擊次數，並按點擊數量從高到低排序
    @Query("SELECT avc.article.articleId, COUNT(avc) AS viewCount " +
            "FROM ArticleViewCount avc " +
            "WHERE avc.clickArticleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY avc.article.articleId " +
            "ORDER BY viewCount DESC")
    List<Object[]> findViewCountsByDateRange(Date startDate, Date endDate);

    @Query("SELECT avc FROM ArticleViewCount avc " +
            "WHERE avc.article.articleId = :articleId AND avc.acct = :acct AND avc.clickArticleDate = :clickArticleDate")
    Optional<ArticleViewCount> findByAcctAndArticleIdAndViewDate(@Param("articleId") Integer articleId,
                                                                 @Param("acct")String acct,
                                                                 @Param("clickArticleDate") Date clickArticleDate);

}
