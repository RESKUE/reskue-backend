package reskue.task;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.base.BaseEntity;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;
import reskue.ReskueController;
import reskue.comment.CommentEntity;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;
import reskue.user.UserService;

/**
 * 
 * The TaskController provides API functions for TaskEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - all functions of the ReskueController in reskue
 *  - finding all subtasks of a task
 *  - finding all users that are helping on a task
 *  - changing the state of a task
 *  - adding a user to the helpers of a task
 *  - removing a user from the helpers of a task
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + TaskController.ROUTE)
public class TaskController extends ReskueController<TaskEntity, TaskRepository, TaskService>{
	
	/**
	 * The API route for TaskEntites.
	 */
	public static final String ROUTE = "/task";
	
	/**
	 * The UserService needed to get the user for the automatic task contact.
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * Creates a task and automatically sets the contact to the user that send the create request.
	 * 
	 * @param request - the request send by the user.
	 * @param response - the response.
	 * @return - the created task with the contact.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IOException
	 */
	@PostMapping("/autoContact")
	@RolesAllowed("administrator")
	public ResponseEntity<TaskEntity> createAutoContact(
			HttpServletRequest request, 
			HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		Utility.LOG.trace("NotificationController.create called.");
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		UserEntity contact = userService.me(subject);
		
		String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		Class<TaskEntity> entityClass = this.service.getEntityClass();
		TaskEntity entity = entityClass.getDeclaredConstructor().newInstance();
		entity = BaseEntity.createEntityFromJSON(body, entity.getUpdateableFields(), entityClass);
		entity.setContactUser(contact);
		
		TaskEntity created = this.service.create(entity);
		return ResponseEntity.ok().body(created);
		
	}
	
	/**
	 * Find all comments of the task.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the task's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + TaskEntity.ID + "}/comments")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CommentEntity> getAllComments(
			@PathVariable(value = TaskEntity.ID) Long id,
			@RequestParam Optional<String[]> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		Utility.LOG.trace("ReskueController.getAllComments called.");
		
		EntitySpecification<CommentEntity> specification = null;
		if (filter.isPresent()) {
			
			specification = new EntitySpecification<CommentEntity>(filter.get());
			
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);

		return service.getAllComments(id, specification, pageable);
		
	}
	
	/**
	 * Find all subtasks of the task.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the task's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + TaskEntity.ID + "}/subtasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<SubtaskEntity> getAllSubtasks(
			@PathVariable(value = TaskEntity.ID) Long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("TaskController.getAllSubtasks called.");
		
		EntitySpecification<SubtaskEntity> specification = null;
		if (filter.isPresent()) {
			
			specification = new EntitySpecification<SubtaskEntity>(filter.get());
			
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getAllSubtasks(id, specification, pageable);
			
	}
	
	/**
	 * Find all users that are working on the task.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the task's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + TaskEntity.ID + "}/helpers")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserEntity> getAllHelpers(
			@PathVariable(value = TaskEntity.ID) Long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("TaskController.getAllHelpers called.");
		
		EntitySpecification<UserEntity> specification = null;
		if (filter.isPresent()) {
			
			specification = new EntitySpecification<UserEntity>(filter.get());
			
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getAllHelpers(id, specification, pageable);
		
	}
	
	/**
	 * Change the state of the task.
	 * 
	 * @param id - the task's identifier.
	 * @param state - the new state.
	 * @return The task after state was changed.
	 */
	@PutMapping("/{" + TaskEntity.ID + "}/changeState/{" + TaskEntity.STATE + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<TaskEntity> changeState(
			@PathVariable(value = TaskEntity.ID) Long id,
			@PathVariable(value = TaskEntity.STATE) int state) {
		
		Utility.LOG.trace("TaskController.changeState called.");
		
		TaskEntity updatedEntity = service.changeState(id, state);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	/**
	 * Adds a user to the helpers that are working on the task.
	 * 
	 * @param id - the task's identifier.
	 * @param helperId - the user's identifier.
	 * @return The task after the user was added.
	 */
	@PutMapping("/{" + TaskEntity.ID + "}/assignHelper/{" + TaskEntity.HELPER_USERS + "}")
	@RolesAllowed({ "administrator" })
	public ResponseEntity<TaskEntity> addHelper(
			@PathVariable(value = TaskEntity.ID) Long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) Long helperId) {
		
		Utility.LOG.trace("TaskController.addHelper called.");
		
		TaskEntity updatedEntity = service.addHelper(id, helperId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	/**
	 * Removes a user from the helpers that are working on the task.
	 * 
	 * @param id - the task's identifier.
	 * @param helperId - the user's identifier.
	 * @return The task after the user was removed.
	 */
	@PutMapping("/{" + TaskEntity.ID + "}/removeHelper/{" + TaskEntity.HELPER_USERS + "}")
	@RolesAllowed({ "administrator" })
	public ResponseEntity<TaskEntity> removeHelper(
			@PathVariable(value = TaskEntity.ID) Long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) Long helperId) {
		
		Utility.LOG.trace("TaskController.removeHelper called.");
		
		TaskEntity updatedEntity = service.removeHelper(id, helperId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
