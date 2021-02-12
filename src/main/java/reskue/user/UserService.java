package reskue.user;

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
import reskue.usergroup.UserGroupEntity;

@Service
public class UserService extends BaseService<UserEntity, UserRepository>{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void init() {
		
	}

	@SuppressWarnings("unchecked")
	public Page<UserGroupEntity> getAllUserGroups(long id, EntitySpecification<UserGroupEntity> specification,
			Pageable pageable) {
		
		UserEntity entity = this.findById(id);

		List<UserGroupEntity> userGroups = entity.getUserGroups();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<UserGroupEntity> criteriaQuery = criteriaBuilder.createQuery(UserGroupEntity.class);
		Root<UserGroupEntity> root = criteriaQuery.from(UserGroupEntity.class);

		userGroups = userGroups.stream().filter(
				(Predicate<? super UserGroupEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<UserGroupEntity> page = new PageImpl<UserGroupEntity>(userGroups, pageable, userGroups.size());

		return page;
		
	}

}
