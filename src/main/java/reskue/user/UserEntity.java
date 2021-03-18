package reskue.user;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kueres.base.BaseEntity;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;

/**
 * 
 * The UserEntity is a representation of a user.
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
public class UserEntity extends BaseEntity<UserEntity>{
	
	@Override
	public String[] getUpdateableFields() {
		return new String[] {
			UserEntity.NAME,
			UserEntity.USER_GROUPS
		};
	}
	
	/**
	 * The name of the user.
	 */
	@Column(name = "name", nullable = false)
	private String name = "unnamed";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 * The identifier used by keycloak for the user.
	 */
	@JsonIgnore
	@Column(name = "keycloakId", nullable = true)
	private String keycloakId = null;
	public static final String KEYCLOAK_ID = "keycloakId";
	public String getKeycloakId() { return this.keycloakId; }
	public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }
	
	/**
	 * The list of tasks that the user is a contact of.
	 */
	@OneToMany(mappedBy = "contactUser", cascade = CascadeType.MERGE)
	@JsonIdentityReference(alwaysAsId = true)
	private List<TaskEntity> taskContact = new ArrayList<TaskEntity>();
	public static final String TASK_CONTACT = "taskContact";
	public List<TaskEntity> getTaskContact() { return this.taskContact; }
	public void setTaskContact(List<TaskEntity> taskContact) { this.taskContact = taskContact; }
	
	/**
	 * The list of tasks that the user is a helper of.
	 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "task_helpers",
			joinColumns = @JoinColumn(name = "helper_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id")
	)
	@JsonIdentityReference(alwaysAsId = true)
	private List<TaskEntity> taskHelper = new ArrayList<TaskEntity>();
	public static final String TASK_HELPER = "taskHelper";
	public List<TaskEntity> getTaskHelper() { return this.taskHelper; }
	public void setTaskHelper(List<TaskEntity> taskHelper) { this.taskHelper = taskHelper; }
	
	/**
	 * The list of comments that the user is an author of.
	 */
	@OneToMany(mappedBy = "author", cascade = CascadeType.MERGE)
	@JsonIdentityReference(alwaysAsId = true)
	private List<CommentEntity> commentAuthor = new ArrayList<CommentEntity>();
	public static final String COMMENT_AUTHOR = "commentAuthor";
	public List<CommentEntity> getCommentAuthor() { return this.commentAuthor; }
	public void setCommentAuthor(List<CommentEntity> commentAuthor) { this.commentAuthor = commentAuthor; }
	
	/**
	 * The list of user groups that the user is a part of.
	 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "usergroup_users",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "usergroup_id", referencedColumnName = "id")
	)
	@JsonIdentityReference(alwaysAsId = true)
	private List<UserGroupEntity> userGroups = new ArrayList<UserGroupEntity>();
	public static final String USER_GROUPS = "userGroups";
	public List<UserGroupEntity> getUserGroups() { return this.userGroups; }
	public void setUserGroups(List<UserGroupEntity> userGroups) { this.userGroups = userGroups; }
	
	/**
	 * The list of notifications that the user has send.
	 */
	@OneToMany(mappedBy = "sender", cascade = CascadeType.MERGE)
	@JsonIdentityReference(alwaysAsId = true)
	private List<NotificationEntity> notificationSender = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_SENDER = "notificationSender";
	public List<NotificationEntity> getNotificationSender() { return this.notificationSender; }
	public void setNotificationSender(List<NotificationEntity> notificationSender) { this.notificationSender = notificationSender; }
	
	@Override
	public void applyPatch(String json) throws JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {
		
		UserEntity details = UserEntity.createEntityFromJSON(json, this.getUpdateableFields(), UserEntity.class);
		
		if (this.containsFields(json, UserEntity.NAME)) {
			this.setName(details.getName());
		}
		if (this.containsFields(json, UserEntity.USER_GROUPS)) {
			this.setUserGroups(details.getUserGroups());
		}
		
	}

}
