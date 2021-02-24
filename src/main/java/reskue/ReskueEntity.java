package reskue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.comment.CommentEntity;

@MappedSuperclass
public abstract class ReskueEntity<E extends ReskueEntity<E>> extends BaseEntity<E>{
	
	@Column(name = "name", nullable = false)
	private String name = "unnamed";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@Column(name = "description", nullable = false, columnDefinition="TEXT")
	private String description = "";
	public static final String DESCRIPTION = "description";
	public String getDescription() { return this.description; }
	public void setDescription(String description) { this.description = description; }
	
	// use 0 to 5 as default
	@Column(name = "priority", nullable = false)
	private int priority = 0;
	public static final String PRIORITY = "priority";
	public int getPriority() { return this.priority; }
	public void setPriority(int priority) { this.priority = priority; }
	
	// use 1 for true and 0 for false
	@Column(name = "isEndangered", nullable = false)
	private int isEndangered = 0;
	public static final String IS_ENDANGERED = "isEndangered";
	public int getIsEndangered() { return this.isEndangered; }
	public void setIsEndangered(int isEndangered) { this.isEndangered = isEndangered; }
	
//	@ElementCollection(targetClass = String.class)
//	@Column(name = "tags", nullable = false)
//	private Set<String> tags = new HashSet<String>();
//	public static final String TAGS = "tags";
//	public Set<String> getTags() { return this.tags; }
//	public void setTags(Set<String> tags) { this.tags = tags; }
	
	@ElementCollection(targetClass = MediaEntity.class)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CommentEntity> comments = new ArrayList<CommentEntity>();
	public static final String COMMENTS = "comments";
	@JsonIgnore
	public List<CommentEntity> getComments() { return this.comments; }
	public void setComments(List<CommentEntity> comments) { this.comments = comments; }
	
	@ElementCollection(targetClass = MediaEntity.class)
	@Column(name = "media", nullable = false)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	@JsonIgnore
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
}
