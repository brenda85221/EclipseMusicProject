package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "article")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "articleId")
	private Integer articleId;

	@Column(name = "articleTitle")
	private String articleTitle;

	@Column(name = "articleContent")
	private String articleContent;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "forumTypeId")
	private ForumType forumType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "articleTypeId")
	private ArticleType articleType;

	@Column(name = "acct")
	private String acct;

	@Column(name = "articlePostTime", updatable = false, insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date articlePostTime;

	@Column(name = "articleUpdateTime", updatable = false, insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date articleUpdateTime;

	@Column(name = "imgPath")
	private String imgPath;

	@Column(name = "visitCount")
	private int visitCount;

	@Column(name = "articleLikedCount")
	private int articleLikedCount;

	@Column(name = "articleIsDeleted")
	private int articleIsDeleted;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	private Set<Report> reports = new LinkedHashSet<Report>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	private Set<Comment> comment = new LinkedHashSet<Comment>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	private Set<ArticleViewCount> articleViewCount = new LinkedHashSet<ArticleViewCount>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	private Set<ArticleLikeCount> articleLikeCount = new LinkedHashSet<ArticleLikeCount>();
	
	public Article() {
		super();
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}

	public ForumType getForumType() { return forumType; }

	public void setForumType(ForumType forumType) {
		this.forumType = forumType;
	}

	public ArticleType getArticleType() {
		return articleType;
	}

	public void setArticleType(ArticleType articleTypeId) {
		this.articleType = articleTypeId;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public Date getArticlePostTime() {
		return articlePostTime;
	}

	public void setArticlePostTime(Date articlePostTime) {
		this.articlePostTime = articlePostTime;
	}

	public Date getArticleUpdateTime() {
		return articleUpdateTime;
	}

	public void setArticleUpdateTime(Date articleUpdateTime) {
		this.articleUpdateTime = articleUpdateTime;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public int getArticleLikedCount() {
		return articleLikedCount;
	}

	public void setArticleLikedCount(int articleLikedCount) {
		this.articleLikedCount = articleLikedCount;
	}

	public int getArticleIsDeleted() {
		return articleIsDeleted;
	}

	public void setArticleIsDeleted(int articleIsDeleted) {
		this.articleIsDeleted = articleIsDeleted;
	}

	@Override
	public String toString() {
		return "ArticleBean{" +
				"articleId=" + articleId +
				", articleTitle='" + articleTitle + '\'' +
				", articleContent='" + articleContent + '\'' +
				", forumTypeBean=" + (forumType != null ? forumType.getForumTypeId() : null) +
				", articleTypeBean=" + (articleType != null ? articleType.getArticleTypeId() : null) +
				", acct='" + acct + '\'' +
				", articlePostTime=" + articlePostTime +
				", imgPath='" + imgPath + '\'' +
				", visitCount=" + visitCount +
				", articleLikedCount=" + articleLikedCount +
				", articleIsDeleted=" + articleIsDeleted +
				'}';
	}

}
