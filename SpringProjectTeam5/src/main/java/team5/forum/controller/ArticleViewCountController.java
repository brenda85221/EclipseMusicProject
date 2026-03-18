package team5.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.ArticleViewCount;
import team5.forum.service.ArticleViewCountService;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum/articleViewCount")
//@CrossOrigin(origins = "*") // 設置前端應用允許的來源
public class ArticleViewCountController {

    @Autowired
    private ArticleViewCountService avcService;

    @GetMapping("/getAll")
    public List<ArticleViewCount> processQueryAllArticleViewCount() {
        return avcService.getArticleViewCount();
    }

    @GetMapping("/get/{articleId}")
    public List<ArticleViewCount> processQueryArticleViewCountByArticleId(@PathVariable("articleId") Integer articleId) {
        return avcService.getArticleViewCountByArticleId(articleId);
    }

    @GetMapping("/getCount/{articleId}")
    public long countArticleViewCountByArticleId(@PathVariable("articleId") Integer articleId){
        return avcService.getArticleViewCountTotalByArticleId(articleId);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addArticleViewCount(@RequestBody ArticleViewCount articleViewCount){
        avcService.insertArticleViewCount(articleViewCount);
        return ResponseEntity.ok("文章點擊紀錄新增成功(若今日無紀錄)");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateArticleViewCount(@RequestBody ArticleViewCount articleViewCount){
        avcService.updateArticleViewCount(articleViewCount);
        return ResponseEntity.ok("文章點擊紀錄更新成功");
    }

    @GetMapping("/getByDate")
    public List<Map<String, Object>> getArticleViewCountByDateList(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Date start = Date.valueOf(startDate);

        Date end = Date.valueOf(endDate);
        return avcService.getArticleViewCountByDate(start, end);
    }



}
