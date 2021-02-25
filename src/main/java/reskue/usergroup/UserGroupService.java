package reskue.usergroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
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
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;
import reskue.notification.NotificationEntity;
import reskue.user.UserEntity;
import reskue.user.UserService;

@Service
public class UserGroupService extends BaseService<UserGroupEntity, UserGroupRepository> {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	protected UserService userService;

	@Override
	@PostConstruct
	public void init() {
		this.identifier = UserGroupController.ROUTE;
		this.routingKey = UserGroupController.ROUTE;
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

		EventConsumer.sendEvent("UserGroupService.getAllUsers", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}
	
	@SuppressWarnings("unchecked")
	public Page<NotificationEntity> getNotifications(long[] ids, EntitySpecification<NotificationEntity> specification,
			Pageable pageable) {

		Utility.LOG.trace("UserGroupService.getNotifications called.");

		List<NotificationEntity> receivedNotifications = new ArrayList<NotificationEntity>();

//		for (int i = 0; i < ids.length; i++) {
//
//			UserGroupEntity userGroup = this.findById(ids[i]);
//
//			List<NotificationEntity> userGroupNotifications = userGroup.getNotificationReceiver();
//
//			userGroupNotifications.forEach((NotificationEntity userGroupNotification) -> {
//
//				if (!receivedNotifications.contains(userGroupNotification)) {
//					receivedNotifications.add(userGroupNotification);
//				}
//
//			});
//
//		}

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<NotificationEntity> criteriaQuery = criteriaBuilder.createQuery(NotificationEntity.class);
			Root<NotificationEntity> root = criteriaQuery.from(NotificationEntity.class);

			receivedNotifications = receivedNotifications.stream()
					.filter((Predicate<? super NotificationEntity>) specification.toPredicate(root, criteriaQuery,
							criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(receivedNotifications, pageable,
				receivedNotifications.size());

		EventConsumer.sendEvent("UserGroupService.getNotifications", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	public UserGroupEntity addUser(long id, long userId) {

		Utility.LOG.trace("UserGroupService.addUser called.");

		UserGroupEntity entity = this.findById(id);
		UserEntity user = userService.findById(userId);

		List<UserEntity> newUsers = entity.getUsers();

		// if the user is already a user
		if (newUsers.contains(user)) {
			newUsers.add(user);
			entity.setUsers(newUsers);
		} else {
			return entity;
		}

		final UserGroupEntity updatedEntity = repository.save(entity);

		EventConsumer.sendEvent("UserGroupService.addUser", EventType.UPDATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	public UserGroupEntity removeUser(long id, long userId) {

		Utility.LOG.trace("UserGroupService.removeUser called.");

		UserGroupEntity entity = this.findById(id);
		UserEntity user = userService.findById(userId);

		List<UserEntity> newUsers = entity.getUsers();

		// if the user is actually a user
		if (newUsers.contains(user)) {
			newUsers.remove(user);
			entity.setUsers(newUsers);
		} else {
			return entity;
		}

		final UserGroupEntity updatedEntity = repository.save(entity);

		EventConsumer.sendEvent("UserGroupService.removeUser", EventType.UPDATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

}
