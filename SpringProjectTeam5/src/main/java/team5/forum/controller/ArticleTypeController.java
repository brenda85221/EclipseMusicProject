package team5.forum.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.ArticleType;
import team5.forum.service.ArticleTypeService;

import java.util.List;

@RestController
@RequestMapping("/forum/articleType")
//@CrossOrigin(origins = "*")
public class ArticleTypeController {

    @Autowired
    private ArticleTypeService articleTypeService;

    @GetMapping("/getAll")
    public List<ArticleType> processQueryAllArticleTypes() {
        return articleTypeService.getAllArticleTypes();
    }

    @GetMapping("/get/{id}")
    public ArticleType processQueryArticleType(@PathVariable("id") Integer articleTypeId) {
        return articleTypeService.getArticleTypeById(articleTypeId);
    }

    @PostMapping("/add")
    public String addArticleType(@RequestBody ArticleType articleTypeBean) {
        System.out.println(articleTypeBean.getArticleTypeName());
        ArticleType articleType = articleTypeService.createArticleType(articleTypeBean);
        if (articleType != null) {
            return "文章類別新增成功, id= " + articleType.getArticleTypeId();
        }
        return "文章類別新增失敗";
    }

    @PutMapping("/update")
    public String updateArticleType(@RequestBody ArticleType articleTypeBean) {
        ArticleType articleType = articleTypeService.updateArticleType(articleTypeBean);
        if(articleType == null){
            return "文章更新失敗";
        }
        return "文章更新成功, " + articleType.toString();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteArticleTypeById(@PathVariable("id") Integer articleTypeId) {
        ArticleType articleType = articleTypeService.getArticleTypeById(articleTypeId);
        if (articleType != null) {
            articleTypeService.deleteArticleTypeById(articleTypeId);
            return "文章類別編號：" + articleType.getArticleTypeId() + "已成功刪除";
        }
        return "查無此文章類別，刪除失敗";
    }

    @GetMapping("/exist/{articleTypeName}")
    public boolean checkIfArticleTypeExist(@PathVariable("articleTypeName") String articleTypeName) {
        System.out.println("接收到的名稱：" + articleTypeName); // 確認是否正常解碼
        return articleTypeService.existsByArticleTypeName(articleTypeName);
        // 如果返回true，代表已存在
    }



}
