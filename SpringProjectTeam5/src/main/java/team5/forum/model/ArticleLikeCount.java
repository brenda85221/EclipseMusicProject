package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "articleLikeCount")
public class ArticleLikeCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleLikeCountId")
    private Integer articleLikeCountId;

    @Column(name = "acct")
    private String acct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articleId")
    private Article article;

    @Column(name = "likeArticleDate", updatable = false, insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    private Date likeArticleDate;

    public ArticleLikeCount() {
    }

    public ArticleLikeCount(String acct, Article article) {
        this.acct = acct;
        this.article = article;
    }

    public Integer getArticleLikeCountId() {
        return articleLikeCountId;
    }

    public void setArticleLikeCountId(Integer articleLikeCountId) {
        this.articleLikeCountId = articleLikeCountId;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Date getLikeArticleDate() {
        return likeArticleDate;
    }

    public void setLikeArticleDate(Date likeArticleDate) {
        this.likeArticleDate = likeArticleDate;
    }
}
