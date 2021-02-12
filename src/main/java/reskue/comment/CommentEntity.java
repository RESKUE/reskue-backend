package reskue.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.user.UserEntity;

@Entity
public class CommentEntity extends BaseEntity<CommentEntity>{
	
	
	/**
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "comments")
	private ReskueEntity reskueEntity;
	public static final String RESKUE_ENTITY = "reskueEntity";
	public ReskueEntity getReskueEntity() { return this.reskueEntity; }
	public void setReskueEntity(ReskueEntity reskueEntity) { this.reskueEntity = reskueEntity; }
	**/
	
	
	// currently no relation
	
//	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
//	@JoinColumn(name = "comments")
	@Column(name = "reskueEntityJSON", nullable = false)
	private String reskueEntityJSON;
	public static final String RESKUE_ENTITY = "reskueEntityJSON";
	public String getReskueEntityJSON() { return this.reskueEntityJSON; }
	public void setReskueEntityJSON(String reskueEntityJSON) { this.reskueEntityJSON = reskueEntityJSON; }
	
	@Column(name = "text", nullable = false)
	private String text = "";
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "media_entity_id")
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "commentAuthor")
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
		
		String text = details.getText();
		List<MediaEntity> media = details.getMedia();
		UserEntity author = details.getAuthor();
		Date createdAt = details.getCreatedAt();
		Date updatedAt = details.getUpdatedAt();
		
		if (text != null) {
			this.setText(text);
		}
		if (media != null) {
			this.setMedia(media);
		}
		if (author != null) {		
			this.setAuthor(author);	
		}
		if (createdAt != null) {
			this.setCreatedAt(createdAt);
		}
		if (updatedAt != null) {
			this.setUpdatedAt(updatedAt);
		}
		
	}

}
