package reskue.usergroup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
import reskue.notification.NotificationEntity;
import reskue.user.UserEntity;

/**
 * 
 * The UserGroupEntity is a representation of a group of users.
 *
 * @author Jan Stra&szlig;burg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Entity
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
	@ManyToMany(mappedBy = "userGroups")
	private List<UserEntity> users = new ArrayList<UserEntity>();
	public static final String USERS = "users";
	@JsonIgnore
	public List<UserEntity> getUsers() { return this.users; }
	public void setUsers(List<UserEntity> users) { this.users = users; }
	
	/**
	 * The list of notifications that every user of the user group should receive.
	 */
	@ManyToMany(mappedBy = "receivers")
	private List<NotificationEntity> notificationReceiver = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_RECEIVER = "notificationReceiver";
	@JsonIgnore
	public List<NotificationEntity> getNotificationReceiver() { return this.notificationReceiver; }
	public void setNotificationReceiver(List<NotificationEntity> notificationReceiver) { this.notificationReceiver = notificationReceiver; }

	@Override
	public void applyPatch(UserGroupEntity details) {
		
		String name = details.getName();
		List<UserEntity> users = details.getUsers();
		
		if (name != null) {
			this.setName(name);
		}
		if (users != null) {
			this.setUsers(users);
		}
		
	}

}
