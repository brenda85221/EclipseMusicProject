package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@Entity
@Table(name = "report")
public class Report implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reportId")
	private Integer reportId;

	@Column(name = "acct")
	private String acct;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "articleId")
	private Article article;

	@Column(name = "reportTime", updatable = false, insertable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date reportTime;

	@Column(name = "reportReason")
	private String reportReason;

	@Column(name = "reportSuccess")
	private int reportSuccess;
	
	public Report() {
		super();
	}

	public Report(String acct, Article article, String reportReason) {
		this.acct = acct;
		this.article = article;
		this.reportReason = reportReason;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
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

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public int getReportSuccess() {
		return reportSuccess;
	}

	public void setReportSuccess(int reportSuccess) {
		this.reportSuccess = reportSuccess;
	}

	public String getReportReason() {
		return reportReason;
	}

	public void setReportReason(String reportReason) {
		this.reportReason = reportReason;
	}
}
