package team5.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.Article;
import team5.forum.model.ArticleLikeCount;
import team5.forum.model.DTO.AddLikeNSendNotifyDTO;
import team5.forum.service.ArticleLikeCountService;
import team5.forum.service.ArticleService;
import team5.forum.service.NotificationService;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum/articleLikeCount")
//@CrossOrigin(origins = "*") // 設置前端應用允許的來源
public class ArticleLikeCountController {

    @Autowired
    private ArticleLikeCountService alcService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getAll")
    public List<ArticleLikeCount> processQueryAllArticleLikeCount() {
        return alcService.getArticleLikeCount();
    }

    @GetMapping("/get/{articleId}")
    public List<ArticleLikeCount> processQueryArticleLikeCountByArticleId(@PathVariable("articleId") Integer articleId) {
        return alcService.getArticleLikeCountByArticleId(articleId);
    }

    @GetMapping("/getCount/{articleId}")
    public long countArticleLikeCountByArticleId(@PathVariable("articleId") Integer articleId){
        return alcService.getArticleLikeCountTotalByArticleId(articleId);
    }

    @GetMapping("/isExist")
    public boolean processQueryIfArticleLikeCountIsExist(@RequestParam String acct, @RequestParam Integer articleId){
        return alcService.checkIfArticleLikeCountExist(acct, articleId);
    }

//    @PostMapping("/add")
//    public ResponseEntity<String> addArticleLikeCount(@RequestBody ArticleLikeCount articleLikeCount){
//
//        alcService.insertArticleLikeCount(articleLikeCount);
//        return ResponseEntity.ok("文章按讚紀錄新增成功");
//    }

    @PostMapping("/add")
    public ResponseEntity<String> addArticleLikeCount(@RequestBody AddLikeNSendNotifyDTO addLikeDTO){
        Article article = articleService.getArticleById(addLikeDTO.getArticleId());
        ArticleLikeCount articleLikeCount = new ArticleLikeCount(addLikeDTO.getAcct(), article);
        alcService.insertArticleLikeCount(articleLikeCount);
        // 發送通知給文章作者
        notificationService.notifyUser(addLikeDTO.getArticleAcct(), addLikeDTO.getMessage(), addLikeDTO.getArticleId());
        System.out.println("訊息發送成功");
        return ResponseEntity.ok("文章按讚紀錄新增成功");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteArticleLikeCount(@RequestBody AddLikeNSendNotifyDTO addLikeDTO){
        Article article = articleService.getArticleById(addLikeDTO.getArticleId());
        ArticleLikeCount articleLikeCount = new ArticleLikeCount(addLikeDTO.getAcct(), article);
        alcService.deleteArticleLikeCount(articleLikeCount);
        return ResponseEntity.ok("文章按讚紀錄刪除成功");
    }

    @GetMapping("/getByDate")
    public List<Map<String, Object>> getArticleViewCountByDateList(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Date start = Date.valueOf(startDate);

        Date end = Date.valueOf(endDate);
        return alcService.getArticleLikeCountByDate(start, end);
    }
}
