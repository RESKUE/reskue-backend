package reskue.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;

@Entity
public class UserEntity extends BaseEntity<UserEntity>{
	
	@Column(name = "name", nullable = false)
	private String name = "";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@Column(name = "keycloakId", nullable = false)
	private String keycloakId = "";
	public static final String KEYCLOAK_ID = "keycloakId";
	public String getKeycloakId() { return this.keycloakId; }
	public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TaskEntity> taskContact = new ArrayList<TaskEntity>();
	public static final String TASK_CONTACT = "taskContact";
	@JsonIgnore
	public List<TaskEntity> getTaskContact() { return this.taskContact; }
	public void setTaskContact(List<TaskEntity> taskContact) { this.taskContact = taskContact; }
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "helperUsers", joinColumns = @JoinColumn(name = "task_entity_id"), inverseJoinColumns = @JoinColumn(name = "user_entity_id"))
	private List<TaskEntity> taskHelper = new ArrayList<TaskEntity>();
	public static final String TASK_HELPER = "taskHelper";
	@JsonIgnore
	public List<TaskEntity> getTaskHelper() { return this.taskHelper; }
	public void setTaskHelper(List<TaskEntity> taskHelper) { this.taskHelper = taskHelper; }
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL)
	private List<CommentEntity> commentAuthor = new ArrayList<CommentEntity>();
	public static final String COMMENT_AUTHOR = "commentAuthor";
	@JsonIgnore
	public List<CommentEntity> getCommentAuthor() { return this.commentAuthor; }
	public void setCommentAuthor(List<CommentEntity> commentAuthor) { this.commentAuthor = commentAuthor; }
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_group_entity_id"), inverseJoinColumns = @JoinColumn(name = "user_entity_id"))
	private List<UserGroupEntity> userGroups = new ArrayList<UserGroupEntity>();
	public static final String USER_GROUPS = "userGroups";
	@JsonIgnore
	public List<UserGroupEntity> getUserGroups() { return this.userGroups; }
	public void setUserGroups(List<UserGroupEntity> userGroups) { this.userGroups = userGroups; }
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL)
	private List<NotificationEntity> notificationSender = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_SENDER = "notificationSender";
	@JsonIgnore
	public List<NotificationEntity> getNotificationSender() { return this.notificationSender; }
	public void setNotificationSender(List<NotificationEntity> notificationSender) { this.notificationSender = notificationSender; }
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "receivers", joinColumns = @JoinColumn(name = "notification_entity_id"), inverseJoinColumns = @JoinColumn(name = "user_entity_id"))
	private List<NotificationEntity> notificationReceiver = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_RECEIVER = "notificationReceiver";
	@JsonIgnore
	public List<NotificationEntity> getNotificationReceiver() { return this.notificationReceiver; }
	public void setNotificationReceiver(List<NotificationEntity> notificationReceiver) { this.notificationReceiver = notificationReceiver; }
	
	@Override
	public void applyPatch(UserEntity details) {
		
		String name = details.getName();
		String keycloakId = details.getName();
		List<TaskEntity> taskContact = details.getTaskContact();
		List<TaskEntity> taskHelper = details.getTaskHelper();
		List<CommentEntity> commentAuthor = details.getCommentAuthor();
		List<UserGroupEntity> userGroups = details.getUserGroups();
		List<NotificationEntity> notificationSender = details.getNotificationSender();
		List<NotificationEntity> notificationReceiver = details.getNotificationReceiver();
		
		if (name != null) {
			this.setName(name);
		}
		if (keycloakId != null) {
			this.setKeycloakId(keycloakId);
		}
		if (taskContact != null) {
			this.setTaskContact(taskContact);
		}
		if (taskHelper != null) {
			this.setTaskHelper(taskHelper);
		}
		if (commentAuthor != null) {
			this.setCommentAuthor(commentAuthor);
		}
		if (userGroups != null) {
			this.setUserGroups(userGroups);
		}
		if (notificationSender != null) {
			this.setNotificationSender(notificationSender);
		}
		if (notificationReceiver != null) {
			this.setNotificationReceiver(notificationReceiver);
		}
		
	}

}
