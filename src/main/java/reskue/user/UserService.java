package reskue.user;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
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
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.utility.Utility;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;

/**
 * 
 * The UserService provides services needed by the UserController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Service
public class UserService extends BaseService<UserEntity, UserRepository>{
	
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
		this.identifier = UserController.ROUTE;
		this.routingKey = UserController.ROUTE;
	}
	
	/**
	 * Finds a user based on a given keycloakId.
	 * 
	 * @param keycloakId - the keycloakId of the user.
	 * @return The user based on the given keycloakId.
	 */
	public UserEntity me(String keycloakId) {
		
		List<UserEntity> userEntities = this.repository.findAll(specification);
		
		for (UserEntity userEntity : userEntities) {
			if (userEntity.getKeycloakId().equals(keycloakId)) {
				return userEntity;
			}
		}
		
		return null;
		
	}
	
	/**
	 * Get all tasks where the user is a contact.
	 * 
	 * @param id - the user's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<TaskEntity> getTasksWhereUserIsContact(long id, EntitySpecification<TaskEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getTasksWhereUserIsContact called.");
		
		UserEntity entity = this.findById(id);
		List<TaskEntity> contactTasks = entity.getTaskContact();
	
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);
			Root<TaskEntity> root = criteriaQuery.from(TaskEntity.class);

			contactTasks = contactTasks.stream().filter(
					(Predicate<? super TaskEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<TaskEntity> page = new PageImpl<TaskEntity>(contactTasks, pageable, contactTasks.size());
		
		EventConsumer.sendEvent("UserService.getTasksWhereUserIsContact", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
	/**
	 * Get all tasks where the user is a helper.
	 * 
	 * @param id - the user's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<TaskEntity> getTasksWhereUserIsHelper(long id, EntitySpecification<TaskEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getTasksWhereUserIsHelper called.");
		
		UserEntity entity = this.findById(id);
		List<TaskEntity> helperTasks = entity.getTaskHelper();
	
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);
			Root<TaskEntity> root = criteriaQuery.from(TaskEntity.class);

			helperTasks = helperTasks.stream().filter(
					(Predicate<? super TaskEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<TaskEntity> page = new PageImpl<TaskEntity>(helperTasks, pageable, helperTasks.size());
		
		EventConsumer.sendEvent("UserService.getTasksWhereUserIsHelper", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
	/**
	 * Get all comments where the user is the author.
	 * 
	 * @param id - the user's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<CommentEntity> getCommentsByUser(long id, EntitySpecification<CommentEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getCommentsByUser called.");		
		
		UserEntity entity = this.findById(id);
		List<CommentEntity> authorComments = entity.getCommentAuthor();
	
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);
			Root<CommentEntity> root = criteriaQuery.from(CommentEntity.class);

			authorComments = authorComments.stream().filter(
					(Predicate<? super CommentEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<CommentEntity> page = new PageImpl<CommentEntity>(authorComments, pageable, authorComments.size());
		
		EventConsumer.sendEvent("UserService.getCommentsByUser", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
	/**
	 * Get all user groups where the user is a member.
	 * 
	 * @param id - the user's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<UserGroupEntity> getUserGroupsForUser(long id, EntitySpecification<UserGroupEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getUserGroupsForUser called.");				
		
		UserEntity entity = this.findById(id);
		List<UserGroupEntity> userGroups = entity.getUserGroups();
	
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<UserGroupEntity> criteriaQuery = criteriaBuilder.createQuery(UserGroupEntity.class);
			Root<UserGroupEntity> root = criteriaQuery.from(UserGroupEntity.class);

			userGroups = userGroups.stream().filter(
					(Predicate<? super UserGroupEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<UserGroupEntity> page = new PageImpl<UserGroupEntity>(userGroups, pageable, userGroups.size());
		
		EventConsumer.sendEvent("UserService.getUserGroupsForUser", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
	/**
	 * Get all notifications where the user is the sender.
	 * 
	 * @param id - the user's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<NotificationEntity> getNotificationsSendByUser(long id, EntitySpecification<NotificationEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getNotificationsSendByUser called.");	
		
		UserEntity entity = this.findById(id);
		List<NotificationEntity> senderNotifiactions = entity.getNotificationSender();
	
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<NotificationEntity> criteriaQuery = criteriaBuilder.createQuery(NotificationEntity.class);
			Root<NotificationEntity> root = criteriaQuery.from(NotificationEntity.class);

			senderNotifiactions = senderNotifiactions.stream().filter(
					(Predicate<? super NotificationEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(senderNotifiactions, pageable, senderNotifiactions.size());
		
		EventConsumer.sendEvent("UserService.getNotificationsSendByUser", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}

}
