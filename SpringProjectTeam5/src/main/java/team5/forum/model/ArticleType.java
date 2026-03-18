package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "articleType")
public class ArticleType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "articleTypeId")
	private Integer articleTypeId;

	@Column(name = "articleTypeName")
	private String articleTypeName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleType")
	private Set<Article> article = new LinkedHashSet<Article>();
	
	public ArticleType() {
		super();
	}

	public Integer getArticleTypeId() {
		return articleTypeId;
	}

	public void setArticleTypeId(Integer articleTypeId) {
		this.articleTypeId = articleTypeId;
	}

	public String getArticleTypeName() {
		return articleTypeName;
	}

	public void setArticleTypeName(String articleTypeName) {
		this.articleTypeName = articleTypeName;
	}

	@Override
	public String toString() {
		return "ArticleTypeBean{" +
				"articleTypeId=" + articleTypeId +
				", articleTypeName='" + articleTypeName + '\'' +
				", articleBean=" + article +
				'}';
	}
}
