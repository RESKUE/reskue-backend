package reskue.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import kueres.base.BaseEntity;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;

public class UserEntity extends BaseEntity<UserEntity>{
	
	@Column(name = "name", nullable = false)
	private String name;
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@Column(name = "keycloakId", nullable = false)
	private String keycloakId;
	public static final String KEYCLOAK_ID = "keycloakId";
	public String getKeycloakId() { return this.keycloakId; }
	public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }
	
	@OneToMany(mappedBy = "contactUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "taskContact", nullable = false)
	private List<TaskEntity> taskContact = new ArrayList<TaskEntity>();
	public static final String TASK_CONTACT = "taskContact";
	public List<TaskEntity> getTaskContact() { return this.taskContact; }
	public void setTaskContact(List<TaskEntity> taskContact) { this.taskContact = taskContact; }
	
	@ManyToMany(mappedBy = "helperUsers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "taskHelper", nullable = false)
	private List<TaskEntity> taskHelper = new ArrayList<TaskEntity>();
	public static final String TASK_HELPER = "taskHelper";
	public List<TaskEntity> getTaskHelper() { return this.taskHelper; }
	public void setTaskHelper(List<TaskEntity> taskHelper) { this.taskHelper = taskHelper; }
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "commentAuthor", nullable = false)
	private List<CommentEntity> commentAuthor = new ArrayList<CommentEntity>();
	public static final String COMMENT_AUTHOR = "commentAuthor";
	public List<CommentEntity> getCommentAuthor() { return this.commentAuthor; }
	public void setCommentAuthor(List<CommentEntity> commentAuthor) { this.commentAuthor = commentAuthor; }
	
	@ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "userGroups", nullable = false)
	private List<UserGroupEntity> userGroups = new ArrayList<UserGroupEntity>();
	public static final String USER_GROUPS = "userGroups";
	public List<UserGroupEntity> getUserGroups() { return this.userGroups; }
	public void setUserGroups(List<UserGroupEntity> userGroups) { this.userGroups = userGroups; }
	
	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "notificationSender", nullable = false)
	private List<NotificationEntity> notificationSender = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_SENDER = "notificationSender";
	public List<NotificationEntity> getNotificationSender() { return this.notificationSender; }
	public void setNotificationSender(List<NotificationEntity> notificationSender) { this.notificationSender = notificationSender; }
	
	@ManyToMany(mappedBy = "receivers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "notificationReceiver", nullable = false)
	private List<NotificationEntity> notificationReceiver = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_RECEIVER = "notificationReceiver";
	public List<NotificationEntity> getNotificationReceiver() { return this.notificationReceiver; }
	public void setNotificationReceiver(List<NotificationEntity> notificationReceiver) { this.notificationReceiver = notificationReceiver; }
	
	@Override
	public void applyPatch(UserEntity details) {
		// TODO Auto-generated method stub
		
	}

}
