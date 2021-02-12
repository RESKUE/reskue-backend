package reskue.usergroup;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.query.EntitySpecification;
import reskue.user.UserEntity;

@Service
public class UserGroupService extends BaseService<UserGroupEntity, UserGroupRepository>{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void init() {
		
	}
	
	@SuppressWarnings("unchecked")
	public Page<UserEntity> getAllUsers(long id, EntitySpecification<UserEntity> specification, Pageable pageable) {

		UserGroupEntity entity = this.findById(id);

		List<UserEntity> users = entity.getUsers();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
		Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

		users = users.stream().filter(
				(Predicate<? super UserEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<UserEntity> page = new PageImpl<UserEntity>(users, pageable, users.size());

		return page;
		
	}

	public UserGroupEntity addUser(long id, long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserGroupEntity removeUser(long id, long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
