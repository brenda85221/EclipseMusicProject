package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.ArticleLikeCount;
import team5.forum.model.ArticleViewCount;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class ArticleLikeCountService {

    @Autowired
    private ArticleLikeCountRepository alcRepo;

    // 查詢全部
    public List<ArticleLikeCount> getArticleLikeCount(){
        return alcRepo.findAll();
    }

    // 根據文章編號查詢每一筆按讚紀錄
    public List<ArticleLikeCount> getArticleLikeCountByArticleId(Integer id){
        return alcRepo.findByArticle_ArticleId(id);
    }

    // 根據文章編號統計按讚次數
    public long getArticleLikeCountTotalByArticleId(Integer id){
        return alcRepo.countByArticleId(id);
    }

    // 查詢是否已對某篇文章按讚
    public boolean checkIfArticleLikeCountExist(String acct, Integer id){
        ArticleLikeCount isExist = alcRepo.findByAcctAndArticle_ArticleId(acct, id);
        return isExist != null;
    }

    // 新增紀錄
    public void insertArticleLikeCount(ArticleLikeCount articleLikeCount){
        LocalDate localDate = LocalDate.now();
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // 檢查是否已存在記錄
        Optional<ArticleLikeCount> existingView = alcRepo.findByArticle_ArticleIdAndAcct(articleLikeCount.getArticle().getArticleId(), articleLikeCount.getAcct());
        if (existingView.isEmpty()) {
            // 如果不存在，新增記錄
            ArticleLikeCount avc = alcRepo.save(articleLikeCount);
        } else {
            System.out.println("The view is already recorded for today.");
        }
    }

    // 刪除紀錄
    public void deleteArticleLikeCount(ArticleLikeCount articleLikeCount){
        // 檢查是否已存在記錄
        Optional<ArticleLikeCount> existingView = alcRepo.findByArticle_ArticleIdAndAcct(articleLikeCount.getArticle().getArticleId(), articleLikeCount.getAcct());

        if (existingView.isPresent()) {
            // 如果存在，刪除記錄
            alcRepo.deleteById(existingView.get().getArticleLikeCountId());
            System.out.println("按讚資料已刪除");
        } else {
            System.out.println("查無這筆資料");
        }
    }

    // 查詢某一天或某一段時間內每篇文章的點擊次數，並按點擊數量從高到低排序
    public List<Map<String, Object>> getArticleLikeCountByDate(Date startDate, Date endDate){
        List<Object[]> results = alcRepo.findLikeCountsByDateRange(startDate, endDate);
        // 將 Object[] 轉換為 Map 格式
        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("articleId", result[0]);
            map.put("viewCount", result[1]);
            return map;
        }).toList();
    }
}
