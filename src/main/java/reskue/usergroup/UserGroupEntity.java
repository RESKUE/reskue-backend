package reskue.usergroup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
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
	public static final String NOTIFICATION_RECEIVER = "users";
	@JsonIgnore
	public List<UserEntity> getUsers() { return this.users; }
	public void setUsers(List<UserEntity> users) { this.users = users; }

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
