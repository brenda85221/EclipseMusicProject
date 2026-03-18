package team5.forum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.Article;
import team5.forum.model.DTO.PopularityScoreDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private ArticleViewCountRepository avcRepo;

    @Autowired
    private ArticleLikeCountRepository alcRepo;

    @Autowired
    private CommentRepository commentRepo;

    public Page<Article> getAllArticles(Pageable pageable){
        return articleRepo.findByArticleIsDeleted(pageable,0);
    }

//    public Page<Article> getArticlesByForumType(String search, Pageable pageable, Integer forumTypeId){
//        if (search == null || search.isEmpty()) {
//            return articleRepo.findByArticleIsDeletedAndForumType_ForumTypeId(pageable,0,forumTypeId);// 如果沒有搜尋字串，返回所有資料
//        }
//        // 根據搜尋字串過濾資料
//        return articleRepo.searchWithForumType(search, pageable, 0, forumTypeId);
//    }
    public Page<Article> getArticlesByForumType(String search, Pageable pageable, Integer forumTypeId){
        if (search == null || search.isEmpty()) {
            return articleRepo.findByArticleIsDeletedAndForumType_ForumTypeIdAndForumType_ForumTypeNameNot(pageable,0,forumTypeId, "公告");// 如果沒有搜尋字串，返回所有資料
        }
        // 根據搜尋字串過濾資料
        return articleRepo.searchWithDeletedFlagAndOptionalForumType(search, pageable, 0, forumTypeId);
    }

    public Page<Article> getAllRemovedArticles(String search, Pageable pageable){
        if (search == null || search.isEmpty()) {
            return articleRepo.findByArticleIsDeleted(pageable, 1);
        }
        // 根據搜尋字串過濾資料
        return articleRepo.searchWithDeletedFlag(search, pageable, 1);
    }

    public Article getArticleById(Integer id){
        return articleRepo.findById(id).orElse(null);
    }

    public Article insertArticle(Article article){
        return articleRepo.save(article);
    }

    public Article updateArticle(Article article){
        return articleRepo.save(article);
    }

    public void deleteArticle(Integer id){
        articleRepo.deleteById(id);
    }

    public Page<Article> getArticles(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return articleRepo.findByArticleIsDeleted(pageable, 0); // 如果沒有搜尋字串，返回所有資料
        }
        // 根據搜尋字串過濾資料
        return articleRepo.searchWithDeletedFlag(search, pageable, 0);
    }

    public Page<Article> getArticlesNoAnno(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return articleRepo.findByArticleIsDeletedAndForumType_ForumTypeNameNot(pageable, 0, "公告"); // 如果沒有搜尋字串，返回所有資料
        }
        // 根據搜尋字串過濾資料
        return articleRepo.searchWithDeletedFlag(search, pageable, 0);
    }

    // 排行榜計算
    public List<PopularityScoreDTO> getTopArticles() {
        // 獲取當前日期，計算一周前的日期
        Date oneWeekAgo = new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);

        // 獲取最近一周發佈的文章
        List<Article> articles = articleRepo.findByArticleIsDeletedAndArticlePostTimeAfter(0, oneWeekAgo);
        List<PopularityScoreDTO> results = new ArrayList<>();

        for (Article article : articles) {
            long visitCount = avcRepo.countByArticleId(article.getArticleId());
            long likeCount = alcRepo.countByArticleId(article.getArticleId());
            long commentCount = commentRepo.countByArticleId(article.getArticleId());

            long popularityScore = visitCount + likeCount * 2 + commentCount * 3;
            results.add(new PopularityScoreDTO(article.getArticleId(), article.getArticleTitle(), visitCount, likeCount, commentCount, popularityScore));
        }

        // 排序：按人氣分數降序排列
        results.sort(Comparator.comparingLong(PopularityScoreDTO::getPopularityScore).reversed());

        // 獲取前10條數據
        int limit = Math.min(10, results.size());
        return results.subList(0, limit);
    }

    // 熱門文章計算
    public List<PopularityScoreDTO> getHotArticles() {
        Pageable pageable = PageRequest.of(0, 10);
        // 獲取最近一周發佈的文章
        Page<Article> articles = articleRepo.findByArticleIsDeleted(pageable, 0);
        List<PopularityScoreDTO> results = new ArrayList<>();

        for (Article article : articles) {
            long visitCount = avcRepo.countByArticleId(article.getArticleId());
            long likeCount = alcRepo.countByArticleId(article.getArticleId());
            long commentCount = commentRepo.countByArticleId(article.getArticleId());

            long popularityScore = visitCount + likeCount * 2 + commentCount * 3;
            results.add(new PopularityScoreDTO(article.getArticleId(), article.getArticleTitle(), visitCount, likeCount, commentCount, popularityScore));
        }

        // 排序：按人氣分數降序排列
        results.sort(Comparator.comparingLong(PopularityScoreDTO::getPopularityScore).reversed());

        // 獲取前10條數據
//        int limit = Math.min(10, results.size());
        return results;
    }

    public String getAcctByArticleId(Integer id){
        Article article = articleRepo.findById(id).orElse(null);
        if (article == null) {
            return null;
        }
        return article.getAcct();
    }

    // 根據acct取得文章
    public List<Article> getArticleByAcct(String acct){
        return articleRepo.findByAcct(acct);
    }

    public List<Article> searchArticlesWithSorting(
            String acct,
            Integer forumTypeId,
            Date startTime,
            Date endTime,
            String search,
            String sortDirection) {
        // 根據 sortDirection 生成 Sort 條件
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "articlePostTime");

        // 調用 Repository 方法
        return articleRepo.findByAcctAndOptionalFilters(
                acct, forumTypeId, startTime, endTime, search, sort);
    }

    // 取得公告文章
    public List<Article> getArticleByForumTypeName(String forumTypeName){
        return  articleRepo.findByForumType_ForumTypeName(forumTypeName);
    }

}




