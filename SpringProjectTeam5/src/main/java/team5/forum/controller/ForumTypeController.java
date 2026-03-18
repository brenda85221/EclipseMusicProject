package team5.forum.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.ForumType;
import team5.forum.service.ForumTypeService;

import java.util.List;

@RestController
@RequestMapping("/forum/forumType")
//@CrossOrigin(origins = "*")
public class ForumTypeController {

    @Autowired
    private ForumTypeService forumTypeService;

    @GetMapping("/getAll")
    public List<ForumType> processQueryAllForumTypes() {
        return forumTypeService.getAllForumTypes();
    }


    @GetMapping("/get/{id}")
    public ForumType processQueryArticleType(@PathVariable("id") Integer id) {
        return forumTypeService.getForumTypeById(id);
    }

    @PostMapping("/add")
    public String addArticleType(@RequestBody ForumType forumTypeBean) {
        ForumType forumType = forumTypeService.createForumType(forumTypeBean);
        if (forumType != null) {
            return "文章類別新增成功, id= " + forumType.getForumTypeId();
        }
        return "文章類別新增失敗";
    }

    @PutMapping("/update")
    public String updateArticleType(@RequestBody ForumType forumTypeBean) {
        ForumType forumType = forumTypeService.updateForumType(forumTypeBean);
        if(forumType == null){
            return "文章更新失敗";
        }
        return "文章更新成功, " + forumType.toString();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteArticleTypeById(@PathVariable("id") Integer id) {
        ForumType forumType = forumTypeService.getForumTypeById(id);

        if (forumType == null) {
            return "查無此討論區類別，刪除失敗";
        }
        forumTypeService.deleteForumTypeById(id);  // 刪除操作

        return "討論區類別編號：" + forumType.getForumTypeId() + " 已成功刪除";
    }

    @GetMapping("/exist/{forumTypeName}")
    public boolean checkIfForumTypeExist(@PathVariable("forumTypeName") String forumTypeName) {
        System.out.println("接收到的名稱：" + forumTypeName); // 確認是否正常解碼
        return forumTypeService.existsByForumTypeName(forumTypeName);
        // 如果返回true，代表已存在
    }

}
