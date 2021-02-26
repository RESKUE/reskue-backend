package reskue.usergroup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kueres.base.BaseEntity;
import reskue.notification.NotificationEntity;
import reskue.user.UserEntity;

/**
 * 
 * The UserGroupEntity is a representation of a group of users.
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
public class UserGroupEntity extends BaseEntity<UserGroupEntity>{
	
	/**
	 * The name of the user group.
	 */
	@Column(name = "name", nullable = false)
	private String name = "unnamed";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 * The list of users that are part of the user group.
	 */
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "usergroup_users",
			joinColumns = @JoinColumn(name = "usergroup_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
	)
	@JsonIdentityReference(alwaysAsId = true)
	private List<UserEntity> users = new ArrayList<UserEntity>();
	public static final String USERS = "users";
	public List<UserEntity> getUsers() { return this.users; }
	public void setUsers(List<UserEntity> users) { this.users = users; }
	
	/**
	 * The list of notifications that every user of the user group should receive.
	 */
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "notification_receivers",
			joinColumns = @JoinColumn(name = "receiver_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id")
	)
	@JsonIdentityReference(alwaysAsId = true)
	private List<NotificationEntity> notificationReceiver = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_RECEIVER = "notificationReceiver";
	public List<NotificationEntity> getNotificationReceiver() { return this.notificationReceiver; }
	public void setNotificationReceiver(List<NotificationEntity> notificationReceiver) { this.notificationReceiver = notificationReceiver; }

	@Override
	public void applyPatch(UserGroupEntity details) {
		
		String name = details.getName();
		List<UserEntity> users = details.getUsers();
		List<NotificationEntity> notifications = details.getNotificationReceiver();
		
		if (name != "unnamed" || this.getName() != "unnamed") {
			this.setName(name);
		}
		if (users != null) {
			this.setUsers(users);
		}
		if (notifications != null) {
			this.setNotificationReceiver(notifications);
		}
		
	}

}
