package reskue.user;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

/**
 * 
 * The UserController provides API functions for UserEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - finding all tasks a user is a contact of
 *  - finding all tasks a user is a helper of
 *  - finding all comments a user is an author of
 *  - finding all user groups a user is a part of
 *  - finding all notifications a user has send
 *  - finding all notifications a user should receive
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + UserController.ROUTE)
public class UserController extends BaseController<UserEntity, UserRepository, UserService>{
	
	/**
	 * The API route for UserEntities.
	 */
	public static final String ROUTE = "/user";
	
	/**
	 * Gets the current user that sends the requests.
	 * 
	 * @param request - the request send by the user.
	 * @param response - the response.
	 * @return - the current user.
	 */
	@GetMapping("/me")
	@RolesAllowed({"administrator", "helper"})
	public ResponseEntity<UserEntity> me(HttpServletRequest request, HttpServletResponse response) {
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		
		UserEntity userEntity = this.service.me(subject);
		
		return ResponseEntity.ok().body(userEntity);
		
	}
	
	/**
	 * Find all tasks that the user is a contact of.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the user's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + UserEntity.ID + "}/contactTasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getTasksWhereUserIsContact(
			@PathVariable(value = UserEntity.ID) Long id,
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
	
	/**
	 * Find all tasks that the user is a helper of.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the user's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + UserEntity.ID + "}/helperTasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getTasksWhereUserIsHelper(
			@PathVariable(value = UserEntity.ID) Long id,
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
	
	/**
	 * Find all comments that the user is an author of.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the user's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + UserEntity.ID + "}/userComments")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CommentEntity> getCommentsByUser(
			@PathVariable(value = UserEntity.ID) Long id,
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
	
	/**
	 * Find all user groups that the user is a part of.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the user's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + UserEntity.ID + "}/userGroups")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserGroupEntity> getUserGroupsForUser(
			@PathVariable(value = UserEntity.ID) Long id,
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
	
	/**
	 * Find all notifications that the user has send.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the user's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + UserEntity.ID + "}/sendNotifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getNotificationsSendByUser(
			@PathVariable(value = UserEntity.ID) Long id,
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

}
