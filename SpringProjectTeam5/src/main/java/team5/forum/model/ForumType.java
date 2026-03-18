package team5.forum.model;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "forumType")
public class ForumType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "forumTypeId")
	private Integer forumTypeId;

	@Column(name = "forumTypeName")
	private String forumTypeName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "forumType")
	private Set<Article> article = new LinkedHashSet<Article>();
	
	public ForumType() {
		super();
	}

	public Integer getForumTypeId() {
		return forumTypeId;
	}

	public void setForumTypeId(Integer forumTypeId) {
		this.forumTypeId = forumTypeId;
	}

	public String getForumTypeName() {
		return forumTypeName;
	}

	public void setForumTypeName(String forumTypeName) {
		this.forumTypeName = forumTypeName;
	}

	@Override
	public String toString() {
		return "ForumTypeBean{" +
				"forumTypeId=" + forumTypeId +
				", forumTypeName='" + forumTypeName + '\'' +
				'}';
	}
}
