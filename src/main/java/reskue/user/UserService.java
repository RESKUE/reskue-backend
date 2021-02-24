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
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;

@Service
public class UserService extends BaseService<UserEntity, UserRepository>{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void init() {
		
	}

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

	@SuppressWarnings("unchecked")
	public Page<NotificationEntity> getNotificationsForUser(long id,
			EntitySpecification<NotificationEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("UserService.getNotificationsForUser called.");	
		
		UserEntity entity = this.findById(id);
		List<NotificationEntity> receiverNotifications = entity.getNotificationReceiver();
	
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<NotificationEntity> criteriaQuery = criteriaBuilder.createQuery(NotificationEntity.class);
			Root<NotificationEntity> root = criteriaQuery.from(NotificationEntity.class);

			receiverNotifications = receiverNotifications.stream().filter(
					(Predicate<? super NotificationEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(receiverNotifications, pageable, receiverNotifications.size());
		
		EventConsumer.sendEvent("UserService.getNotificationsForUser", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
	}

}
