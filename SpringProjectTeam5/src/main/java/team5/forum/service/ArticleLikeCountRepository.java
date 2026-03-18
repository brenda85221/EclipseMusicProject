package team5.forum.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team5.forum.model.ArticleLikeCount;
import team5.forum.model.ArticleViewCount;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Integer> {
    List<ArticleLikeCount> findByArticle_ArticleId(int articleId);
    ArticleLikeCount findByAcctAndArticle_ArticleId(String acct, int articleId);

    // 根據 articleId 查詢按讚數量
    @Query("SELECT COUNT(alc) FROM ArticleLikeCount alc WHERE alc.article.articleId = :articleId")
    long countByArticleId(int articleId);

//    @Query("SELECT alc FROM ArticleLikeCount alc " +
//            "WHERE alc.article.articleId = :articleId AND alc.acct = :acct")
//    Optional<ArticleLikeCount> findByAcctAndArticleIdAndLikeDate(@Param("articleId") Integer articleId,
//                                                                 @Param("acct")String acct);

    Optional<ArticleLikeCount> findByArticle_ArticleIdAndAcct(int articleId, String acct);


    // 查詢某一天或某一段時間內每篇文章的按讚次數，並依按讚數量從高到低排序
    @Query("SELECT alc.article.articleId, COUNT(alc) AS viewCount " +
            "FROM ArticleLikeCount alc " +
            "WHERE alc.likeArticleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY alc.article.articleId " +
            "ORDER BY viewCount DESC")
    List<Object[]> findLikeCountsByDateRange(Date startDate, Date endDate);
}
