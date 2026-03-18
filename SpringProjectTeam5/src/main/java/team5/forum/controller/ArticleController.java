package team5.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team5.forum.model.Article;
import team5.forum.model.ArticleType;
import team5.forum.model.DTO.PopularityScoreDTO;
import team5.forum.model.ForumType;
import team5.forum.service.ArticleService;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping({"/forum/article", "/user/forum/article"})
//@CrossOrigin(origins = "*") // 設置前端應用允許的來源
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Value("${photo.storage.prefix}")
    private String photoPath;

    // 取得所有未下架文章
//    @GetMapping("/getAll")
//    public ResponseEntity<Page<Article>> getArticles(
//            @RequestParam(required = false, defaultValue = "") String search,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Article> articles = articleService.getArticles(search, pageable);
//        // 對每個文章的 forumType 進行檢查和處理
//        Page<Article> processedArticles = articles.map(article -> {
//            if (article.getForumType() == null) {
//                ForumType placeholder = new ForumType();
//                placeholder.setForumTypeName("討論區已不存在");
//                article.setForumType(placeholder);
//            }
//            if (article.getArticleType() == null) {
//                ArticleType placeholder = new ArticleType();
//                placeholder.setArticleTypeName("文章類別已不存在");
//                article.setArticleType(placeholder);
//            }
//            return article;
//        });
//
//        return ResponseEntity.ok(processedArticles);
//    }

    // 由舊至新
    @GetMapping("/getAll")
    public ResponseEntity<Page<Article>> getArticlesByForumType(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer forumTypeId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles;
        // 根據是否有 forumTypeId 決定呼叫的 service 方法
        if (forumTypeId == null) {
            articles = articleService.getArticlesNoAnno(search, pageable);
        } else {
            articles = articleService.getArticlesByForumType(search, pageable, forumTypeId);
        }
        // 對每個文章的 forumType 進行檢查和處理
        Page<Article> processedArticles = articles.map(article -> {
            if (article.getForumType() == null) {
                ForumType placeholder = new ForumType();
                placeholder.setForumTypeName("討論區已不存在");
                article.setForumType(placeholder);
            }
            if (article.getArticleType() == null) {
                ArticleType placeholder = new ArticleType();
                placeholder.setArticleTypeName("文章類別已不存在");
                article.setArticleType(placeholder);
            }
            return article;
        });
        return ResponseEntity.ok(processedArticles);
    }

    // 由新至舊排序
    @GetMapping("/getAllDesc")
    public ResponseEntity<Page<Article>> getArticlesByForumTypeDesc(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer forumTypeId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "articlePostTime"));
        Page<Article> articles;
        // 根據是否有 forumTypeId 決定呼叫的 service 方法
        if (forumTypeId == null) {
            articles = articleService.getArticlesNoAnno(search, pageable);
        } else {
            articles = articleService.getArticlesByForumType(search, pageable, forumTypeId);
        }
        // 對每個文章的 forumType 進行檢查和處理
        Page<Article> processedArticles = articles.map(article -> {
            if (article.getForumType() == null) {
                ForumType placeholder = new ForumType();
                placeholder.setForumTypeName("討論區已不存在");
                article.setForumType(placeholder);
            }
            if (article.getArticleType() == null) {
                ArticleType placeholder = new ArticleType();
                placeholder.setArticleTypeName("文章類別已不存在");
                article.setArticleType(placeholder);
            }
            return article;
        });
        return ResponseEntity.ok(processedArticles);
    }

    // 熱門排序
    @GetMapping("/getAllHot")
    public ResponseEntity<Page<Article>> getArticlesByForumTypeHOT(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer forumTypeId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitCount"));
        Page<Article> articles;
        // 根據是否有 forumTypeId 決定呼叫的 service 方法
        if (forumTypeId == null) {
            articles = articleService.getArticlesNoAnno(search, pageable);
        } else {
            articles = articleService.getArticlesByForumType(search, pageable, forumTypeId);
        }
        // 對每個文章的 forumType 進行檢查和處理
        Page<Article> processedArticles = articles.map(article -> {
            if (article.getForumType() == null) {
                ForumType placeholder = new ForumType();
                placeholder.setForumTypeName("討論區已不存在");
                article.setForumType(placeholder);
            }
            if (article.getArticleType() == null) {
                ArticleType placeholder = new ArticleType();
                placeholder.setArticleTypeName("文章類別已不存在");
                article.setArticleType(placeholder);
            }
            return article;
        });
        return ResponseEntity.ok(processedArticles);
    }

    // 取得所有已下架文章
    @GetMapping("/getAllRemoved")
    public ResponseEntity<Page<Article>> processQueryAllRemovedArticles(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> allArticles = articleService.getAllRemovedArticles(search, pageable);
        return ResponseEntity.ok(allArticles);
    }

    // 透過id查詢單篇文章
    @GetMapping("/get/{articleId}")
    public Article processQueryById(@PathVariable("articleId") Integer id){
        return articleService.getArticleById(id);
    }

    // 新增文章
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> processInsertArticle(@RequestParam("article") String articleJson,
                                       @RequestParam(value = "image", required = false) MultipartFile multipartFile)
            throws IOException {

        // 解析文章 JSON 字符串為 Article 對象
        ObjectMapper objectMapper = new ObjectMapper();
        Article articleBean = objectMapper.readValue(articleJson, Article.class);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + fileName;
            System.out.println(fileName);

            String saveDirPath = photoPath + "forum/articleImg/";
            File saveFileDir = new File(saveDirPath);
            saveFileDir.mkdirs();

            File saveFilePath = new File(saveFileDir, newFileName);
            multipartFile.transferTo(saveFilePath);
            System.out.println("saveFilePath = " + saveFilePath);

            articleBean.setImgPath("img/forum/articleImg/" + newFileName);
        }

        Article article = articleService.insertArticle(articleBean);
        if(article == null){
            // 返回失敗的 JSON
            Map<String, Object> response = new HashMap<>();
            response.put("message", "文章新增失敗");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        // 返回成功的 JSON
        Map<String, Object> response = new HashMap<>();
        response.put("articleId", article.getArticleId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/uploadImg")
    public String processInsertArticleImg(@RequestParam(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + fileName;
            System.out.println(fileName);

            String saveDirPath = photoPath + "forum/articleImg/";
            File saveFileDir = new File(saveDirPath);
            saveFileDir.mkdirs();

            File saveFilePath = new File(saveFileDir, newFileName);
            multipartFile.transferTo(saveFilePath);
            System.out.println("saveFilePath = " + saveFilePath);
            return "img/forum/articleImg/" + newFileName;
        }
        return "圖片為空";
    }

    // 更新文章資訊
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> processUpdateArticle(@RequestParam("article") String articleJson,
                                       @RequestParam(value = "image", required = false) MultipartFile multipartFile)
            throws IOException {

        // 解析文章 JSON 字符串為 Article 對象
        ObjectMapper objectMapper = new ObjectMapper();
        Article articleBean = objectMapper.readValue(articleJson, Article.class);
        System.out.println("Received articleBean: " + articleBean);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + fileName;
            System.out.println(fileName);

            String saveDirPath = photoPath + "forum/articleImg/";
            File saveFileDir = new File(saveDirPath);
            saveFileDir.mkdirs();

            File saveFilePath = new File(saveFileDir, newFileName);
            multipartFile.transferTo(saveFilePath);
            System.out.println("saveFilePath = " + saveFilePath);

            articleBean.setImgPath("img/forum/articleImg/" + newFileName);
        }

        Article article = articleService.updateArticle(articleBean);
        if(article == null){
            // 返回失敗的 JSON
            Map<String, Object> response = new HashMap<>();
            response.put("message", "文章新增失敗");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        // 返回成功的 JSON
        Map<String, Object> response = new HashMap<>();
        response.put("articleId", article.getArticleId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 刪除單篇文章
    @DeleteMapping("/delete/{articleId}")
    public String processDeleteArticle(@PathVariable("articleId") Integer articleId){
        Article article = articleService.getArticleById(articleId);
        if(article != null){
            articleService.deleteArticle(articleId);
            return "文章編號：" + article.getArticleId() + " 已成功刪除";
        }
        return "查無此文章，刪除失敗";
    }

    // 下架文章
    @PutMapping("/remove/{articleId}")
    public String processRemoveArticle(@PathVariable("articleId") Integer articleId){
        Article article = articleService.getArticleById(articleId);
        if(article != null){
            article.setArticleIsDeleted(1);
            articleService.updateArticle(article);
            return "文章編號：" + article.getArticleId() + " 下架成功";
        }
        return "查無此文章編號，下架失敗";
    }

    // 重新上架文章
    @PutMapping("/reset/{articleId}")
    public String processResetArticle(@PathVariable("articleId") Integer articleId){
        Article article = articleService.getArticleById(articleId);
        if(article != null){
            article.setArticleIsDeleted(0);
            articleService.updateArticle(article);
            return "文章編號：" + article.getArticleId() + " 重新上架成功";
        }
        return "查無此文章編號，重新上架失敗";
    }

    @GetMapping("/popular")
    public List<PopularityScoreDTO> getPopularArticles() {
        return articleService.getTopArticles();
    }

    // 根據acct取得文章
    @GetMapping("/getByAcct/{acct}")
    public List<Article> queryArticleByAcct(@PathVariable("acct") String acct){
        return articleService.getArticleByAcct(acct);
    }

    @GetMapping("/getByAcct")
    public ResponseEntity<List<Article>> searchArticlesWithSorting(
            @RequestParam(required = false) String acct,
            @RequestParam(required = false) Integer forumTypeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<Article> articles = articleService.searchArticlesWithSorting(
                acct, forumTypeId, startDate, endDate, search, sortDirection);
        return ResponseEntity.ok(articles);
    }

    // 取得公告文章
    @GetMapping("/getAnno")
    public List<Article> queryAnnouncement(){
        return articleService.getArticleByForumTypeName("公告");
    }


}
