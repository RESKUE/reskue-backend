package reskue.task;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.media.MediaEntity;
import reskue.ReskueEntity;
import reskue.comment.CommentEntity;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;

@Entity
public class TaskEntity extends ReskueEntity<TaskEntity>{
	
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	@ManyToOne
	@JoinColumn(name = "cultural_asset_id", referencedColumnName = "id")
	private CulturalAssetEntity culturalAsset = null;
	public static final String CULTURAL_ASSET = "culturalAsset";
	public CulturalAssetEntity getCulturalAsset() { return this.culturalAsset; }
	public void setCulturalAsset(CulturalAssetEntity culturalAsset) { this.culturalAsset = culturalAsset; }
	
	@OneToMany(mappedBy = "task")
	private List<SubtaskEntity> subtasks = new ArrayList<SubtaskEntity>();
	public static final String SUBTASKS = "subtasks";
	@JsonIgnore
	public List<SubtaskEntity> getSubtasks() { return this.subtasks; }
	public void setSubtasks(List<SubtaskEntity> subtasks) { this.subtasks = subtasks; }
	
	@ManyToOne
	@JoinColumn(name = "task_contact_id", referencedColumnName = "id")
	private UserEntity contactUser = null;
	public static final String CONTACT_USER = "contactUser";
	public UserEntity getContactUser() { return this.contactUser; }
	public void setContactUser(UserEntity contactUser) { this.contactUser = contactUser; }
	
	@ManyToMany(mappedBy = "taskHelper")
	private List<UserEntity> helperUsers = new ArrayList<UserEntity>();
	public static final String HELPER_USERS = "helperUsers";
	@JsonIgnore
	public List<UserEntity> getHelperUsers() { return this.helperUsers; }
	public void setHelperUsers(List<UserEntity> helperUsers) { this.helperUsers = helperUsers; }
	
	@Column(name = "recommendedHelperUsers", nullable = false)
	private int recommendedHelperUsers = 1;
	public static final String RECOMMENDED_HELPER_USERS = "recommendedHelperUsers";
	public int getRecommendedHelperUsers() { return this.recommendedHelperUsers; }
	public void setRecommendedHelperUsers(int recommendedHelperUsers) { this.recommendedHelperUsers = recommendedHelperUsers; }

	@Override
	public void applyPatch(TaskEntity details) {
		
		String name = details.getName();
		String description = details.getDescription();
		
//		Set<String> tags = details.getTags();
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
		
		if (name != null) {
			this.setName(name);
		}
		if (description != null) {
			this.setDescription(description);
		}
		
//		if (tags != null) {
//			this.setTags(tags);
//		}
		this.setPriority(priority);
		this.setIsEndangered(isEndangered);
		
		
		if (comments != null) {
			this.setComments(comments);
		}
		if (media != null) {
			this.setMedia(media);
		}
		
		
		
		this.setState(state);
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
		this.setRecommendedHelperUsers(recommendedHelperUsers);
		
	}

}
