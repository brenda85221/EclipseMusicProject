package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team5.forum.model.Comment;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    public List<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    public List<Comment> getCommentsByArticleId(Integer articleId) {
        return commentRepo.findByArticle_ArticleId(articleId);
    }

    // 根據文章編號統計按讚次數
    public long getCommentTotalByArticleId(Integer id){
        long l = commentRepo.countByArticleId(id);
        System.out.println("result = " + l);
        return l;
    }

    public Comment addComment(Comment comment) {
        return commentRepo.save(comment);
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepo.findById(commentId).orElse(null);
    }

    public void deleteComment(Integer commentId) {
        commentRepo.deleteById(commentId);
    }

}
