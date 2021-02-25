package reskue.task;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;
import reskue.ReskueController;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;

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
 * @since Feb 25, 2021
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
			@PathVariable(value = TaskEntity.ID) long id,
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
			@PathVariable(value = TaskEntity.ID) long id,
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
			@PathVariable(value = TaskEntity.ID) long id,
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
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) long helperId) {
		
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
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) long helperId) {
		
		Utility.LOG.trace("TaskController.removeHelper called.");
		
		TaskEntity updatedEntity = service.removeHelper(id, helperId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
