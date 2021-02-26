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

/**
 * 
 * The UserGroupService provides services needed by the UserGroupController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Service
public class UserGroupService extends BaseService<UserGroupEntity, UserGroupRepository> {
	
	/**
	 * The UserService needed to add users as members of a group.
	 */
	@Autowired
	protected UserService userService;
	
	/**
	 * The EntityManager needed to create a CriteriaBuilder.
	 */
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = UserGroupController.ROUTE;
		this.routingKey = UserGroupController.ROUTE;
	}
	
	/**
	 * Get all users that are members of the user group.
	 * 
	 * @param id - the user group's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
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
	
	/**
	 * Get all notifications for the user group.
	 * 
	 * @param id - the user group's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<NotificationEntity> getNotifications(long[] ids, EntitySpecification<NotificationEntity> specification,
			Pageable pageable) {

		Utility.LOG.trace("UserGroupService.getNotifications called.");

		List<NotificationEntity> receivedNotifications = new ArrayList<NotificationEntity>();
		
		
		for (int i = 0; i < ids.length; i++) {

			UserGroupEntity userGroup = this.findById(ids[i]);

			receivedNotifications.addAll(userGroup.getNotificationReceiver());

		}
		
		receivedNotifications = receivedNotifications.stream().distinct().collect(Collectors.toList());

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
	
	/**
	 * Adds a user as a member to the user group.
	 * 
	 * @param id - the user group's identifier.
	 * @param userId - the user's identifier.
	 * @return The user group after the user was added.
	 */
	public UserGroupEntity addUser(long id, long userId) {

		Utility.LOG.trace("UserGroupService.addUser called.");

		UserGroupEntity entity = this.findById(id);
		UserEntity user = userService.findById(userId);

		List<UserEntity> newUsers = entity.getUsers();

		// if the user is already a user
		if (newUsers.contains(user)) {
			return entity;
		} else {
			newUsers.add(user);
			entity.setUsers(newUsers);
		}

		final UserGroupEntity updatedEntity = repository.save(entity);

		EventConsumer.sendEvent("UserGroupService.addUser", EventType.UPDATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}
	
	/**
	 * Removes a user as a member to the user group.
	 * 
	 * @param id - the user group's identifier.
	 * @param userId - the user's identifier.
	 * @return The user group after the user was removed.
	 */
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
