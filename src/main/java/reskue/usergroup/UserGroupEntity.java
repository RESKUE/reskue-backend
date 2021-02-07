package reskue.usergroup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import kueres.base.BaseEntity;
import reskue.user.UserEntity;

public class UserGroupEntity extends BaseEntity<UserGroupEntity>{
	
	@Column(name = "name", nullable = false)
	private String name;
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@ManyToMany(mappedBy = "userGroups", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "users", nullable = false)
	private List<UserEntity> users = new ArrayList<UserEntity>();
	public static final String NOTIFICATION_RECEIVER = "users";
	public List<UserEntity> getUsers() { return this.users; }
	public void setUsers(List<UserEntity> users) { this.users = users; }

	@Override
	public void applyPatch(UserGroupEntity details) {
		// TODO Auto-generated method stub
		
	}

}
