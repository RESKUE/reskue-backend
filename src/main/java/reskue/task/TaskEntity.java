package reskue.task;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kueres.media.MediaEntity;
import reskue.ReskueEntity;
import reskue.comment.CommentEntity;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;

/**
 * 
 * The TaskEntity is a representation of a task.
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
public class TaskEntity extends ReskueEntity<TaskEntity>{
	
	/**
	 * The state of the task.
	 */
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	/**
	 * The cultural asset the task belongs to.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "cultural_asset_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity culturalAsset = null;
	public static final String CULTURAL_ASSET = "culturalAsset";
	public CulturalAssetEntity getCulturalAsset() { return this.culturalAsset; }
	public void setCulturalAsset(CulturalAssetEntity culturalAsset) { this.culturalAsset = culturalAsset; }
	
	/**
	 * The list of comments associated with the task.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "task_comment",
			joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id")
	)
	@JsonIdentityReference(alwaysAsId = true)
	private List<CommentEntity> comments = new ArrayList<CommentEntity>();
	public static final String COMMENTS = "comments";
	public List<CommentEntity> getComments() { return this.comments; }
	public void setComments(List<CommentEntity> comments) { this.comments = comments; }
	
	/**
	 * The list of subtasks associated with the task.
	 */
	@JsonManagedReference
	@OneToMany(mappedBy = "task", cascade = CascadeType.MERGE)
	private List<SubtaskEntity> subtasks = new ArrayList<SubtaskEntity>();
	public static final String SUBTASKS = "subtasks";
	public List<SubtaskEntity> getSubtasks() { return this.subtasks; }
	public void setSubtasks(List<SubtaskEntity> subtasks) { this.subtasks = subtasks; }
	
	/**
	 * The user that is the contact of the task.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "task_contact_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private UserEntity contactUser = null;
	public static final String CONTACT_USER = "contactUser";
	public UserEntity getContactUser() { return this.contactUser; }
	public void setContactUser(UserEntity contactUser) { this.contactUser = contactUser; }
	
	/**
	 * The list of users that help completing the task.
	 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "task_helpers",
			joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "helper_id", referencedColumnName = "id")
	)
	@JsonIdentityReference(alwaysAsId = true)
	private List<UserEntity> helperUsers = new ArrayList<UserEntity>();
	public static final String HELPER_USERS = "helperUsers";
	public List<UserEntity> getHelperUsers() { return this.helperUsers; }
	public void setHelperUsers(List<UserEntity> helperUsers) { this.helperUsers = helperUsers; }
	
	/**
	 * The recommended number of helpers that are needed to complete the task.
	 */
	@Column(name = "recommendedHelperUsers", nullable = false)
	private int recommendedHelperUsers = 1;
	public static final String RECOMMENDED_HELPER_USERS = "recommendedHelperUsers";
	public int getRecommendedHelperUsers() { return this.recommendedHelperUsers; }
	public void setRecommendedHelperUsers(int recommendedHelperUsers) { this.recommendedHelperUsers = recommendedHelperUsers; }

	@Override
	public void applyPatch(TaskEntity details) {
		
		String name = details.getName();
		String description = details.getDescription();
		int priority = details.getPriority();
		int isEndangered = details.getIsEndangered();		
		List<CommentEntity> comments = details.getComments();
		List<MediaEntity> media = details.getMedia();	
		
		int state = details.getState();
		CulturalAssetEntity culturalAsset = details.getCulturalAsset();
		List<SubtaskEntity> subtasks = details.getSubtasks();
		UserEntity contactUser = details.getContactUser();
		List<UserEntity> helperUsers = details.getHelperUsers();
		int recommendedHelperUsers = details.getRecommendedHelperUsers();
		
		if (name != "unnamed" || this.getName() != "unnamed") {
			this.setName(name);
		}
		if (description != "" || this.getDescription() != "") {
			this.setDescription(description);
		}
		if (priority != 0 || this.getPriority() != 0) {
			this.setPriority(priority);
		}
		if (isEndangered != 0 || this.getIsEndangered() != 0) {
			this.setIsEndangered(isEndangered);	
		}
		if (comments != null) {
			this.setComments(comments);
		}
		if (media != null) {
			this.setMedia(media);
		}	
		
		if (state != 0 || this.getState() != 0) {
			this.setState(state);
		}
		if (culturalAsset != null) {
			this.setCulturalAsset(culturalAsset);
		}
		if (subtasks != null) {
			this.setSubtasks(subtasks);
		}
		if (contactUser != null) {
			this.setContactUser(contactUser);
		}
		if (helperUsers != null) {
			this.setHelperUsers(helperUsers);
		}
		if (recommendedHelperUsers != 1 || this.getRecommendedHelperUsers() != 1) {
			this.setRecommendedHelperUsers(recommendedHelperUsers);
		}
		
	}

}
