package reskue.user;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;


@RestController
@RequestMapping(BaseController.API_ENDPOINT + UserController.ROUTE)
public class UserController extends BaseController<UserEntity, UserRepository, UserService>{
	
	public static final String ROUTE = "/user";

	@GetMapping("/{" + UserEntity.ID + "}/contactTasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getTasksWhereUserIsContact(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserController.getTasksWhereUserIsContact called.");
		
		EntitySpecification<TaskEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<TaskEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getTasksWhereUserIsContact(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/helperTasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getTasksWhereUserIsHelper(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserController.getTasksWhereUserIsHelper called.");
		
		EntitySpecification<TaskEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<TaskEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getTasksWhereUserIsHelper(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/userComments")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CommentEntity> getCommentsByUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserController.getCommentsByUser called.");
		
		EntitySpecification<CommentEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<CommentEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getCommentsByUser(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/userGroups")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserGroupEntity> getUserGroupsForUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserController.getUserGroupsForUser called.");
		
		EntitySpecification<UserGroupEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<UserGroupEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getUserGroupsForUser(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/sendNotifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getNotificationsSendByUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserController.getNotificationsSendByUser called.");
		
		EntitySpecification<NotificationEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<NotificationEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getNotificationsSendByUser(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/receivedNotifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getNotificationsForUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserController.getNotificationsForUser called.");
		
		EntitySpecification<NotificationEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<NotificationEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getNotificationsForUser(id, specification, pageable);
		
	}

}
