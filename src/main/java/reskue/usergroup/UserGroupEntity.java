package reskue.usergroup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
import reskue.user.UserEntity;

@Entity
public class UserGroupEntity extends BaseEntity<UserGroupEntity>{
	
	@Column(name = "name", nullable = false)
	private String name = "";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@ManyToMany(mappedBy = "userGroups", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
