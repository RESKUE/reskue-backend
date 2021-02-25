package reskue.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.user.UserEntity;

/**
 * 
 * The CommentEntity is a representation of comments made by users.
 *
 * @author Jan Straﬂburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Entity
public class CommentEntity extends BaseEntity<CommentEntity>{
	
	
//	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
//	@JoinColumn(name = "comments")
//	private ReskueEntity reskueEntity;
//	public static final String RESKUE_ENTITY = "reskueEntity";
//	public ReskueEntity getReskueEntity() { return this.reskueEntity; }
//	public void setReskueEntity(ReskueEntity reskueEntity) { this.reskueEntity = reskueEntity; }
	
	
	// currently no relation
	
//	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
//	@JoinColumn(name = "comments")
	/**
	 * Unfinished
	 */
	@Column(name = "reskueEntityJSON", nullable = false, columnDefinition="TEXT")
	private String reskueEntityJSON = "";
	public static final String RESKUE_ENTITY = "reskueEntityJSON";
	public String getReskueEntityJSON() { return this.reskueEntityJSON; }
	public void setReskueEntityJSON(String reskueEntityJSON) { this.reskueEntityJSON = reskueEntityJSON; }
	
	/**
	 * The text of the comment.
	 */
	@Column(name = "text", nullable = false, columnDefinition="TEXT")
	private String text = "";
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
	/**
	 * The list of media associated with the comment.
	 */
	@OneToMany
	@JoinColumn(name = "comment_id", referencedColumnName = "id")
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	@JsonIgnore
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
	/**
	 * The author of the comment.
	 */
	@ManyToOne
	@JoinColumn(name = "comment_author_id", referencedColumnName = "id")
	private UserEntity author = null;
	public static final String AUTHOR = "author";
	public UserEntity getAuthor() { return this.author; }
	public void setAuthor(UserEntity author) { this.author = author; }
	
	/**
	 * The time when the comment was created.
	 */
	@Column(name = "createdAt", nullable = false)
	private Date createdAt = new Date();
	public static final String CREATED_AT = "createdAt";
	public Date getCreatedAt() { return this.createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	
	/**
	 * The last time the comment was changed.
	 */
	@Column(name = "updatedAt", nullable = false)
	private Date updatedAt = new Date();
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
