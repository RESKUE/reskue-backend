package reskue.task;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kueres.base.BaseEntity;
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
public class TaskEntity extends ReskueEntity<TaskEntity>{
	
	@Override
	public String[] getUpdateableFields() {
		return new String[] {
				TaskEntity.NAME,
				TaskEntity.DESCRIPTION,
				TaskEntity.PRIORITY,
				TaskEntity.IS_ENDANGERED,
				TaskEntity.STATE,
				TaskEntity.CULTURAL_ASSET,
				TaskEntity.COMMENTS,
				TaskEntity.SUBTASKS,
				TaskEntity.CONTACT_USER,
				TaskEntity.HELPER_USERS,
				TaskEntity.RECOMMENDED_HELPER_USERS,
				TaskEntity.MEDIA
		};
	}
	
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
	@ManyToOne()
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity culturalAsset = null;
	public static final String CULTURAL_ASSET = "culturalAsset";
	public CulturalAssetEntity getCulturalAsset() { return this.culturalAsset; }
	public void setCulturalAsset(CulturalAssetEntity culturalAsset) { this.culturalAsset = culturalAsset; }
	
	/**
	 * The list of comments associated with the task.
	 */
	@OneToMany()
	@JoinTable(
			name = "task_comments",
			joinColumns = { @JoinColumn(name = "task_id", referencedColumnName = BaseEntity.ID) },
			inverseJoinColumns = { @JoinColumn(name = "comment_id", referencedColumnName = BaseEntity.ID) }
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<CommentEntity> comments = new ArrayList<CommentEntity>();
	public static final String COMMENTS = "comments";
	public List<CommentEntity> getComments() { return this.comments; }
	public void setComments(List<CommentEntity> comments) { this.comments = comments; }
	
	/**
	 * The list of subtasks associated with the task.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = SubtaskEntity.class)
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	private List<SubtaskEntity> subtasks = new ArrayList<SubtaskEntity>();
	public static final String SUBTASKS = "subtasks";
	public List<SubtaskEntity> getSubtasks() { return this.subtasks; }
	public void setSubtasks(List<SubtaskEntity> subtasks) { this.subtasks = subtasks; }
	
	/**
	 * The user that is the contact of the task.
	 */
	@ManyToOne()
	@JoinColumn(name = "task_contact_id", referencedColumnName = "id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private UserEntity contactUser = null;
	public static final String CONTACT_USER = "contactUser";
	public UserEntity getContactUser() { return this.contactUser; }
	public void setContactUser(UserEntity contactUser) { this.contactUser = contactUser; }
	
	/**
	 * The list of users that help completing the task.
	 */
	@ManyToMany()
	@JoinTable(
			name = "task_helpers",
			joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "helper_id", referencedColumnName = "id")
	)
	@JsonIgnoreProperties(UserEntity.TASK_HELPER)
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

	/**
	 * The list of media associated with the entity.
	 */
	@OneToMany()
	@JoinTable(
			name = "task_media",
			joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id")
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
	@Override
	public void applyPatch(String json) throws JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {
		
		TaskEntity details = TaskEntity.createEntityFromJSON(json, this.getUpdateableFields(), TaskEntity.class);
		
		if (this.containsFields(json, TaskEntity.NAME)) {
			this.setName(details.getName());
		}
		if (this.containsFields(json, TaskEntity.DESCRIPTION)) {
			this.setDescription(details.getDescription());
		}
		if (this.containsFields(json, TaskEntity.PRIORITY)) {
			this.setPriority(details.getPriority());
		}
		if (this.containsFields(json, TaskEntity.IS_ENDANGERED)) {
			this.setIsEndangered(details.getIsEndangered());	
		}
		if (this.containsFields(json, TaskEntity.COMMENTS)) {
			this.setComments(details.getComments());
		}
		if (this.containsFields(json, TaskEntity.MEDIA)) {
			this.setMedia(details.getMedia());
		}
		
		if (this.containsFields(json, TaskEntity.STATE)) {
			this.setState(details.getState());
		}
		if (this.containsFields(json, TaskEntity.CULTURAL_ASSET)) {
			this.setCulturalAsset(details.getCulturalAsset());
		}
		if (this.containsFields(json, TaskEntity.SUBTASKS)) {
			this.setSubtasks(details.getSubtasks());
		}
		if (this.containsFields(json, TaskEntity.CONTACT_USER)) {
			this.setContactUser(details.getContactUser());
		}
		if (this.containsFields(json, TaskEntity.HELPER_USERS)) {
			this.setHelperUsers(details.getHelperUsers());
		}
		if (this.containsFields(json, TaskEntity.RECOMMENDED_HELPER_USERS)) {
			this.setRecommendedHelperUsers(details.getRecommendedHelperUsers());
		}
		
	}

}