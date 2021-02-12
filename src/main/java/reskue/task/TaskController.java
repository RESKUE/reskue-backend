package reskue.task;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
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
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<SubtaskEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<SubtaskEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}

		Pageable pagination = Pageable.unpaged();
		if (page.isPresent() && size.isPresent()) {
			pagination = PageRequest.of(page.get(), size.get());
		}

		pagination = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sorting);
		
		return service.getAllSubtasks(id, specification, pagination);
			
	}
	
	@GetMapping("/{" + TaskEntity.ID + "}/helpers")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserEntity> getAllHelpers(
			@PathVariable(value = TaskEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<UserEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<UserEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}

		Pageable pagination = Pageable.unpaged();
		if (page.isPresent() && size.isPresent()) {
			pagination = PageRequest.of(page.get(), size.get());
		}

		pagination = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sorting);
		
		return service.getAllHelpers(id, specification, pagination);
		
	}
	
	@PutMapping("/{" + TaskEntity.ID + "}/changeState/{" + TaskEntity.STATE + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<TaskEntity> changeState(
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.STATE) int state, 
			@Valid @RequestBody TaskEntity details)
			throws ResourceNotFoundException {

		TaskEntity updatedEntity = service.changeState(id, state);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + TaskEntity.ID + "}/assignHelper/{" + TaskEntity.HELPER_USERS + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<TaskEntity> addHelper(
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) long helperId, 
			@Valid @RequestBody TaskEntity details)
			throws ResourceNotFoundException {

		TaskEntity updatedEntity = service.addHelper(id, helperId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + TaskEntity.ID + "}/removeHelper/{" + TaskEntity.HELPER_USERS + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<TaskEntity> removeHelper(
			@PathVariable(value = TaskEntity.ID) long id,
			@PathVariable(value = TaskEntity.HELPER_USERS) long helperId, 
			@Valid @RequestBody TaskEntity details)
			throws ResourceNotFoundException {

		TaskEntity updatedEntity = service.removeHelper(id, helperId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
