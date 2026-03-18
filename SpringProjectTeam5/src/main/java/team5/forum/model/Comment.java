package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "commentId")
	private Integer commentId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "articleId")
	private Article article;

	@Column(name = "commentContent")
	private String commentContent;

	@Column(name = "acct")
	private String acct;

	@Column(name = "commentPostTime", updatable = false, insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date commentPostTime;

	@Column(name = "commentUpdateTime", updatable = false, insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date commentUpdateTime;

	@Column(name = "commentLikedCount")
	private Integer commentLikedCount;

	@Column(name = "commentIsDeleted")
	private Integer commentIsDeleted;

	public Comment() {
	}

	public Comment(Article article, String commentContent, String acct) {
		this.article = article;
		this.commentContent = commentContent;
		this.acct = acct;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public Date getCommentPostTime() {
		return commentPostTime;
	}

	public void setCommentPostTime(Date commentPostTime) {
		this.commentPostTime = commentPostTime;
	}

	public Date getCommentUpdateTime() {
		return commentUpdateTime;
	}

	public void setCommentUpdateTime(Date commentUpdateTime) {
		this.commentUpdateTime = commentUpdateTime;
	}

	public Integer getCommentLikedCount() {
		return commentLikedCount;
	}

	public void setCommentLikedCount(Integer commentLikedCount) {
		this.commentLikedCount = commentLikedCount;
	}

	public Integer getCommentIsDeleted() {
		return commentIsDeleted;
	}

	public void setCommentIsDeleted(Integer commentIsDeleted) {
		this.commentIsDeleted = commentIsDeleted;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"commentId='" + commentId + '\'' +
				", article=" + article +
				", commentContent='" + commentContent + '\'' +
				", acct='" + acct + '\'' +
				", commentPostTime='" + commentPostTime + '\'' +
				", commentUpdateTime='" + commentUpdateTime + '\'' +
				", commentLikedCount=" + commentLikedCount +
				", commentIsDeleted=" + commentIsDeleted +
				'}';
	}
}
