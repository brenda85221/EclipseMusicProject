package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.ArticleViewCount;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class ArticleViewCountService {

    @Autowired
    private ArticleViewCountRepository articleViewCountRepo;

    // 查詢全部
    public List<ArticleViewCount> getArticleViewCount(){
        return articleViewCountRepo.findAll();
    }

    // 根據文章編號查詢每一筆紀錄
    public List<ArticleViewCount> getArticleViewCountByArticleId(Integer id){
        return articleViewCountRepo.findByArticle_ArticleId(id);
    }

    // 根據文章編號統計瀏覽次數
    public long getArticleViewCountTotalByArticleId(Integer id){
        return articleViewCountRepo.countByArticleId(id);
    }

    // 新增紀錄
    public void insertArticleViewCount(ArticleViewCount articleViewCount){
        LocalDate localDate = LocalDate.now();
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // 檢查是否已存在記錄
        Optional<ArticleViewCount> existingView = articleViewCountRepo.findByAcctAndArticleIdAndViewDate(articleViewCount.getArticle().getArticleId(), articleViewCount.getAcct(), today);

        if (existingView.isEmpty()) {
            // 如果不存在，新增記錄
            ArticleViewCount avc = articleViewCountRepo.save(articleViewCount);
        } else {
            System.out.println("The view is already recorded for today.");
        }
    }

    public void updateArticleViewCount(ArticleViewCount articleViewCount){
        articleViewCountRepo.save(articleViewCount);
    }

    // 查詢某一天或某一段時間內每篇文章的點擊次數，並按點擊數量從高到低排序
    public List<Map<String, Object>> getArticleViewCountByDate(Date startDate, Date endDate){
        List<Object[]> results = articleViewCountRepo.findViewCountsByDateRange(startDate, endDate);
        // 將 Object[] 轉換為 Map 格式
        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("articleId", result[0]);
            map.put("viewCount", result[1]);
            return map;
        }).toList();
    }



}
