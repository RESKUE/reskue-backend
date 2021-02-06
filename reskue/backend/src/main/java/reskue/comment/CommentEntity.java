package reskue.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.ReskueEntity;
import reskue.user.UserEntity;

public class CommentEntity extends BaseEntity<CommentEntity>{
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "comments")
	private ReskueEntity reskueEntity;
	public static final String RESKUE_ENTITY = "reskueEntity";
	public ReskueEntity getReskueEntity() { return this.reskueEntity; }
	public void setReskueEntity(ReskueEntity reskueEntity) { this.reskueEntity = reskueEntity; }
	
	@Column(name = "text", nullable = false)
	private String text = "";
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
	// needs JSON Converter etc to connect to media entity
	@OneToMany(mappedBy = "reskueEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "media", nullable = false)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@Column(name = "author", nullable = false)
	private UserEntity author;
	public static final String AUTHOR = "author";
	public UserEntity getAuthor() { return this.author; }
	public void setAuthor(UserEntity author) { this.author = author; }
	
	@Column(name = "createdAt", nullable = false)
	private Date createdAt;
	public static final String CREATED_AT = "createdAt";
	public Date getCreatedAt() { return this.createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	
	@Column(name = "updatedAt", nullable = false)
	private Date updatedAt;
	public static final String UPDATED_AT = "updatedAt";
	public Date getUpdatedAt() { return this.updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

	@Override
	public void applyPatch(CommentEntity details) {
		// TODO Auto-generated method stub
		
	}

}
