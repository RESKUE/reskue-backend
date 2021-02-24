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

@RestController
@RequestMapping(BaseController.API_ENDPOINT + TaskController.ROUTE)
public class TaskController extends ReskueController<TaskEntity, TaskRepository, TaskService>{
	
	public static final String ROUTE = "/task";
	
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
	
	@PutMapping("/{" + TaskEntity.ID + "}/changeState/{" + TaskEntity.STATE + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<TaskEntity> changeState(
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.STATE) int state) {
		
		Utility.LOG.trace("TaskController.changeState called.");
		
		TaskEntity updatedEntity = service.changeState(id, state);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + TaskEntity.ID + "}/assignHelper/{" + TaskEntity.HELPER_USERS + "}")
	@RolesAllowed({ "administrator" })
	public ResponseEntity<TaskEntity> addHelper(
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) long helperId) {
		
		Utility.LOG.trace("TaskController.addHelper called.");
		
		TaskEntity updatedEntity = service.addHelper(id, helperId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
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
