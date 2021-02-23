package reskue.usergroup;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;
import reskue.user.UserEntity;
import reskue.user.UserService;

@Service
public class UserGroupService extends BaseService<UserGroupEntity, UserGroupRepository>{
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	protected UserService userService;
	
	@Override
	public void init() {
		
	}
	
	@SuppressWarnings("unchecked")
	public Page<UserEntity> getAllUsers(long id, EntitySpecification<UserEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("UserGroupService.getAllUsers called.");

		UserGroupEntity entity = this.findById(id);
		List<UserEntity> users = entity.getUsers();
		
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
			Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

			users = users.stream().filter(
					(Predicate<? super UserEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<UserEntity> page = new PageImpl<UserEntity>(users, pageable, users.size());

		return page;
		
	}

	public UserGroupEntity addUser(long id, long userId) {
		
		Utility.LOG.trace("UserGroupService.addUser called.");
		
		UserGroupEntity entity = this.findById(id);
		UserEntity user = userService.findById(userId);
		
		List<UserEntity> newUsers = entity.getUsers();
		
		//if the user is already a user
		if(newUsers.contains(user)) {
			newUsers.add(user);
			entity.setUsers(newUsers);
		} else {
			return entity;
		}
		
		final UserGroupEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

	public UserGroupEntity removeUser(long id, long userId) {
		
		Utility.LOG.trace("UserGroupService.removeUser called.");
		
		UserGroupEntity entity = this.findById(id);
		UserEntity user = userService.findById(userId);
		
		List<UserEntity> newUsers = entity.getUsers();
		
		//if the user is actually a user
		if(newUsers.contains(user)) {
			newUsers.remove(user);
			entity.setUsers(newUsers);
		} else {
			return entity;
		}
		
		final UserGroupEntity updatedEntity = repository.save(entity);
		return updatedEntity;
	
	}

}
