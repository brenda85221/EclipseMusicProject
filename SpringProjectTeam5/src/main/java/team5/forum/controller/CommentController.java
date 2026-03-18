package team5.forum.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.Article;
import team5.forum.model.Comment;
import team5.forum.model.DTO.AddCommentNSendNotifyDTO;
import team5.forum.model.DTO.AddLikeNSendNotifyDTO;
import team5.forum.service.ArticleService;
import team5.forum.service.CommentService;
import team5.forum.service.NotificationService;
//import team5.util.forum.JwtTestUtil;

import java.util.List;

@RestController
@RequestMapping("/forum/comment")
//@CrossOrigin(origins = "*") // 設置前端應用允許的來源
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getAll")
    public List<Comment> processQueryAllComments(){
        return commentService.getAllComments();
    }

    @GetMapping("/getByArticleId/{articleId}")
    public List<Comment> processQueryCommentsByArticleId(@PathVariable("articleId") Integer articleId){
        return commentService.getCommentsByArticleId(articleId);
    }

    @GetMapping("/getCount/{articleId}")
    public long countArticleLikeCountByArticleId(@PathVariable("articleId") Integer articleId){
        return commentService.getCommentTotalByArticleId(articleId);
    }

    @PostMapping("/add")
    public Comment processInsert(@RequestBody AddCommentNSendNotifyDTO addCommentDTO){
        Article article = articleService.getArticleById(addCommentDTO.getArticleId());
        Comment comment = new Comment(article, addCommentDTO.getCommentContent(), addCommentDTO.getAcct());
        Comment NewComment = commentService.addComment(comment);
        // 發送通知給文章作者
        notificationService.notifyUser(addCommentDTO.getArticleAcct(), addCommentDTO.getMessage(), addCommentDTO.getArticleId());
        System.out.println("Comment訊息發送成功");
        return NewComment;
    }

    @DeleteMapping("/delete/{id}")
    public String processDeleteComment(@PathVariable("id") Integer commentId){
        // 從 Authorization 標頭中獲取 JWT Token
//        String token = request.getHeader("Authorization").substring(7); // 去掉 "Bearer " 前綴

        // 解析 JWT Token 並獲取 acct
//        String tokenAcct = JwtTestUtil.getSubject(token);
//        System.out.println(tokenAcct);
        Comment comment = commentService.getCommentById(commentId);
//        if (!comment.getAcct().equals(tokenAcct)) {
//            return "You are not authorized to delete this comment";
//        }
        if(comment != null){
            commentService.deleteComment(commentId);
            return "留言編號：" + comment.getCommentId() + " 已成功刪除";
        }
        return "查無此留言，刪除失敗";
    }
}
