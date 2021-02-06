package reskue.task;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import reskue.ReskueEntity;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;

public class TaskEntity extends ReskueEntity<TaskEntity>{
	
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "culturalAsset", nullable = false)
	private CulturalAssetEntity culturalAsset;
	public static final String CULTURAL_ASSET = "culturalAsset";
	public CulturalAssetEntity getCulturalAsset() { return this.culturalAsset; }
	public void setCulturalAsset(CulturalAssetEntity culturalAsset) { this.culturalAsset = culturalAsset; }
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "subtasks", nullable = false)
	private List<SubtaskEntity> subtasks = new ArrayList<SubtaskEntity>();
	public static final String SUBTASKS = "subtasks";
	public List<SubtaskEntity> getSubtasks() { return this.subtasks; }
	public void setSubtasks(List<SubtaskEntity> subtasks) { this.subtasks = subtasks; }
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "contactUser", nullable = false)
	private UserEntity contactUser;
	public static final String CONTACT_USER = "contactUser";
	public UserEntity getContactUser() { return this.contactUser; }
	public void setContactUser(UserEntity contactUser) { this.contactUser = contactUser; }
	
	@ManyToMany(mappedBy = "taskHelper", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "helperUsers", nullable = false)
	private List<UserEntity> helperUsers = new ArrayList<UserEntity>();
	public static final String HELPER_USERS = "helperUsers";
	public List<UserEntity> getHelperUsers() { return this.helperUsers; }
	public void setHelperUsers(List<UserEntity> helperUsers) { this.helperUsers = helperUsers; }
	
	@Column(name = "recommendedHelperUsers", nullable = false)
	private int recommendedHelperUsers = 0;
	public static final String RECOMMENDED_HELPER_USERS = "recommendedHelperUsers";
	public int getRecommendedHelperUsers() { return this.recommendedHelperUsers; }
	public void setRecommendedHelperUsers(int recommendedHelperUsers) { this.recommendedHelperUsers = recommendedHelperUsers; }

	@Override
	public void applyPatch(TaskEntity details) {
		// TODO Auto-generated method stub
		
	}

}
