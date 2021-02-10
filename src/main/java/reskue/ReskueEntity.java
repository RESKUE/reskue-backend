package reskue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.comment.CommentEntity;

public abstract class ReskueEntity<E extends ReskueEntity<E>> extends BaseEntity<E>{
	
	@Column(name = "name", nullable = false)
	private String name = "";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@Column(name = "description", nullable = false)
	private String description = "";
	public static final String DESCRIPTION = "description";
	public String getDescription() { return this.description; }
	public void setDescription(String description) { this.description = description; }
	
	@Column(name = "tags", nullable = false)
	private Set<String> tags = new HashSet<String>();
	public static final String TAGS = "tags";
	public Set<String> getTags() { return this.tags; }
	public void setTags(Set<String> tags) { this.tags = tags; }
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "comments", nullable = false)
	private List<CommentEntity> comments = new ArrayList<CommentEntity>();
	public static final String COMMENTS = "comments";
	public List<CommentEntity> getComments() { return this.comments; }
	public void setComments(List<CommentEntity> comments) { this.comments = comments; }
	
	// needs JSON Converter etc to connect to media entity
	@OneToMany(mappedBy = "reskueEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "media", nullable = false)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
}
