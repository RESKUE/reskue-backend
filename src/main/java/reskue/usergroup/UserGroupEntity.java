package reskue.usergroup;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
public class UserGroupEntity extends BaseEntity<UserGroupEntity>{
	
	@Override
	public String[] getUpdateableFields() {
		return new String[] {
			UserGroupEntity.NAME,
			UserGroupEntity.USERS
		};
	}
	
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
	@ManyToMany()
	@JoinTable(
			name = "usergroup_users",
			joinColumns = @JoinColumn(name = "usergroup_id", referencedColumnName = BaseEntity.ID),
			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = BaseEntity.ID)
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<UserEntity> users = new ArrayList<UserEntity>();
	public static final String USERS = "users";
	public List<UserEntity> getUsers() { return this.users; }
	public void setUsers(List<UserEntity> users) { this.users = users; }
	
	/**
	 * The list of notifications that every user of the user group should receive.
	 */
	@ManyToMany()
	@JoinTable(
			name = "notification_receivers",
			joinColumns = @JoinColumn(name = "receiver_id", referencedColumnName = BaseEntity.ID),
			inverseJoinColumns = @JoinColumn(name = "notification_id", referencedColumnName = BaseEntity.ID)
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<NotificationEntity> notificationReceiver = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATION_RECEIVER = "notificationReceiver";
	public List<NotificationEntity> getNotificationReceiver() { return this.notificationReceiver; }
	public void setNotificationReceiver(List<NotificationEntity> notificationReceiver) { this.notificationReceiver = notificationReceiver; }

	@Override
	public void applyPatch(String json) throws JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {
		
		UserGroupEntity details = UserGroupEntity.createEntityFromJSON(json, this.getUpdateableFields(), UserGroupEntity.class);
		
		if (this.containsFields(json, UserGroupEntity.NAME)) {
			this.setName(details.getName());
		}
		if (this.containsFields(json, UserGroupEntity.USERS)) {
			this.setUsers(details.getUsers());
		}
		
	}

}
