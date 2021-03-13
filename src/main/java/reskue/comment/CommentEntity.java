package reskue.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
 * @since Feb 25, 2021
 *
 */

@Entity
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class CommentEntity extends BaseEntity<CommentEntity>{
	
	/**
	 * The cultural asset the comment belongs to if it belongs to a cultural asset.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "comment_cultural_asset_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity commentCulturalAsset = null;
	public static final String COMMENT_CULTURAL_ASSET = "commentCulturalAsset";
	public CulturalAssetEntity getCommentCulturalAsset() { return this.commentCulturalAsset; }
	public void setCommentCulturalAsset(CulturalAssetEntity commentCulturalAsset) { this.commentCulturalAsset = commentCulturalAsset; }
	
	/**
	 * The task the comment belongs to if it belongs to a task.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "comment_task_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private TaskEntity commentTask = null;
	public static final String COMMENT_TASK = "commentTask";
	public TaskEntity getCommentTask() { return this.commentTask; }
	public void setCommentTask(TaskEntity commentTask) { this.commentTask = commentTask; }
	
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
	@OneToMany(cascade = CascadeType.MERGE)
	@JoinColumn(name = "comment_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
	/**
	 * The author of the comment.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "comment_author_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
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

	/**
	 * Doesnt allow changes to:
	 *  - author
	 *  - createdAt
	 *  - updatedAt
	 *  If both a task and a cultural asset are given the related entity is not changed
	 */
	@Override
	public void applyPatch(CommentEntity details) {
		
		CulturalAssetEntity commentCulturalAsset = details.getCommentCulturalAsset();
		TaskEntity commentTask = details.getCommentTask();
		String text = details.getText();
		List<MediaEntity> media = details.getMedia();
		
		if (commentCulturalAsset != null && commentTask == null) {
			this.setCommentCulturalAsset(commentCulturalAsset);
			this.setCommentTask(null);
		}
		if (commentTask != null && commentCulturalAsset == null) {
			this.setCommentCulturalAsset(null);
			this.setCommentTask(commentTask);
		}
		if (commentTask == null && commentCulturalAsset == null) {
			this.setCommentCulturalAsset(null);
			this.setCommentTask(null);
		}
		if (text != "" || this.getText() != "") {
			this.setText(text);
		}
		if (media != null) {
			this.setMedia(media);
		}
		this.setUpdatedAt(new Date());
		
	}

}
