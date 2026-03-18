package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "articleViewCount")
public class ArticleViewCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleViewCountId")
    private Integer articleViewCountId;

    @Column(name = "acct")
    private String acct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articleId")
    private Article article;

    @Column(name = "clickArticleDate", updatable = false, insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    private Date clickArticleDate;

    public ArticleViewCount() {
    }

    public Integer getArticleViewCountId() {
        return articleViewCountId;
    }

    public void setArticleViewCountId(Integer articleViewCountId) {
        this.articleViewCountId = articleViewCountId;
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

    public Date getClickArticleDate() {
        return clickArticleDate;
    }

    public void setClickArticleDate(Date clickArticleDate) {
        this.clickArticleDate = clickArticleDate;
    }
}
