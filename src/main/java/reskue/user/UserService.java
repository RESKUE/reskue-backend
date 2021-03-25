package reskue.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
		
		List<UserEntity> userEntities = this.repository.findAll();
		
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
	public Page<TaskEntity> getTasksWhereUserIsContact(Long id, EntitySpecification<TaskEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getTasksWhereUserIsContact called.");
		
		UserEntity entity = this.findById(id);
		List<TaskEntity> contactTasks = entity.getTaskContact();
	
		if (specification != null) {

			contactTasks = contactTasks.stream().filter(specification.toPredicate(TaskEntity.class)).collect(Collectors.toList());
			
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
	public Page<TaskEntity> getTasksWhereUserIsHelper(Long id, EntitySpecification<TaskEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getTasksWhereUserIsHelper called.");
		
		UserEntity entity = this.findById(id);
		List<TaskEntity> helperTasks = entity.getTaskHelper();
	
		if (specification != null) {

			helperTasks = helperTasks.stream().filter(specification.toPredicate(TaskEntity.class)).collect(Collectors.toList());
			
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
	public Page<CommentEntity> getCommentsByUser(Long id, EntitySpecification<CommentEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getCommentsByUser called.");		
		
		UserEntity entity = this.findById(id);
		List<CommentEntity> authorComments = entity.getCommentAuthor();
	
		if (specification != null) {

			authorComments = authorComments.stream().filter(specification.toPredicate(CommentEntity.class)).collect(Collectors.toList());
			
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
	public Page<UserGroupEntity> getUserGroupsForUser(Long id, EntitySpecification<UserGroupEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getUserGroupsForUser called.");				
		
		UserEntity entity = this.findById(id);
		List<UserGroupEntity> userGroups = entity.getUserGroups();
	
		if (specification != null) {

			userGroups = userGroups.stream().filter(specification.toPredicate(UserGroupEntity.class)).collect(Collectors.toList());
			
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
	public Page<NotificationEntity> getNotificationsSendByUser(Long id, EntitySpecification<NotificationEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("UserService.getNotificationsSendByUser called.");	
		
		UserEntity entity = this.findById(id);
		List<NotificationEntity> senderNotifiactions = entity.getNotificationSender();
	
		if (specification != null) {

			senderNotifiactions = senderNotifiactions.stream().filter(specification.toPredicate(NotificationEntity.class)).collect(Collectors.toList());
			
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(senderNotifiactions, pageable, senderNotifiactions.size());
		
		EventConsumer.sendEvent("UserService.getNotificationsSendByUser", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}

}
