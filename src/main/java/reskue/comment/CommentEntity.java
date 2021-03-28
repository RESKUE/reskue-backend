package reskue.comment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.task.TaskEntity;
import reskue.user.UserEntity;

/**
 * 
 * The CommentEntity is a representation of comments made by users.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@Entity
public class CommentEntity extends BaseEntity<CommentEntity>{
	
	@Override
	public String[] getUpdateableFields() {
		return new String[] {
			CommentEntity.TEXT,
			CommentEntity.COMMENT_CULTURAL_ASSET,
			CommentEntity.COMMENT_TASK,
			CommentEntity.AUTHOR,
			CommentEntity.MEDIA,
		};
	}
	
	/**
	 * The text of the comment.
	 */
	@Column(name = "text", nullable = true, columnDefinition="TEXT")
	private String text = "uncommented";
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
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
	
	/**
	 * The cultural asset the comment belongs to if it belongs to a cultural asset.
	 */
	@ManyToOne()
	@JoinTable(
			name = "cultural_asset_comments",
			joinColumns = { @JoinColumn(name = "comment_id", referencedColumnName = BaseEntity.ID) },
			inverseJoinColumns = { @JoinColumn(name = "cultural_asset_id", referencedColumnName = BaseEntity.ID) }
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity commentCulturalAsset = null;	
	public static final String COMMENT_CULTURAL_ASSET = "commentCulturalAsset";
	public CulturalAssetEntity getCommentCulturalAsset() { return this.commentCulturalAsset; }
	public void setCommentCulturalAsset(CulturalAssetEntity commentCulturalAsset) { this.commentCulturalAsset = commentCulturalAsset; }
	
	/**
	 * The task the comment belongs to if it belongs to a task.
	 */
	@ManyToOne()
	@JoinTable(
			name = "task_comments",
			joinColumns = { @JoinColumn(name = "comment_id", referencedColumnName = BaseEntity.ID) },
			inverseJoinColumns = { @JoinColumn(name = "task_id", referencedColumnName = BaseEntity.ID) }
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private TaskEntity commentTask = null;
	public static final String COMMENT_TASK = "commentTask";
	public TaskEntity getCommentTask() { return this.commentTask; }
	public void setCommentTask(TaskEntity commentTask) { this.commentTask = commentTask; }
	
	/**
	 * The author of the comment.
	 */
	@ManyToOne()
	@JoinColumn(name = "comment_author_id", referencedColumnName = BaseEntity.ID)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private UserEntity author = null;
	public static final String AUTHOR = "author";
	public UserEntity getAuthor() { return this.author; }
	public void setAuthor(UserEntity author) { this.author = author; }
	
	/**
	 * The list of media associated with the comment.
	 */
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinTable(
			name = "comment_media",
			joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = BaseEntity.ID),
			inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = BaseEntity.ID)
	)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }

	/**
	 * Doesnt allow changes to:
	 *  - author
	 *  - createdAt
	 *  - updatedAt
	 *  
	 *  If both a task and a cultural asset are given the related entity is not changed
	 *  
	 * @throws JsonProcessingException if the JSON string can not be processed
	 * @throws SecurityException if the JSON string can not be processed
	 * @throws NoSuchMethodException if the JSON string can not be processed
	 * @throws InvocationTargetException if the JSON string can not be processed
	 * @throws IllegalArgumentException if the JSON string can not be processed
	 * @throws IllegalAccessException if the JSON string can not be processed
	 * @throws InstantiationException if the JSON string can not be processed
	 * @throws JsonMappingException if the JSON string can not be processed
	 */
	@Override
	public void applyPatch(String json) throws JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {
		
		CommentEntity details = CommentEntity.createEntityFromJSON(json, this.getUpdateableFields(), CommentEntity.class);
		
		if (this.containsFields(json, CommentEntity.TEXT)) {			
			this.setText(details.getText());			
		}
		
		this.setUpdatedAt(new Date());
		
		if (this.containsFields(json, CommentEntity.COMMENT_CULTURAL_ASSET) &&
				!this.containsFields(json, CommentEntity.COMMENT_TASK)) {
			
			this.setCommentCulturalAsset(details.getCommentCulturalAsset());
			this.setCommentTask(null);
			
		} else if (this.containsFields(json, CommentEntity.COMMENT_TASK) &&
				!this.containsFields(json, CommentEntity.COMMENT_CULTURAL_ASSET)) {
			
			this.setCommentTask(details.getCommentTask());
			this.setCommentCulturalAsset(null);
			
		} else if (!this.containsFields(json, CommentEntity.COMMENT_CULTURAL_ASSET) &&
				!this.containsFields(json, CommentEntity.COMMENT_TASK)) {
			
			this.setCommentCulturalAsset(null);
			this.setCommentTask(null);
			
		}
		
		if (this.containsFields(json, CommentEntity.MEDIA)) {			
			this.setMedia(details.getMedia());			
		}
		
	}

}
