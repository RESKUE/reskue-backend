package reskue.backend.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import reskue.backend.routing.RouteService;

public abstract class BaseController<E extends BaseEntity<E>, R extends JpaRepository<E, Long>, S extends BaseService<E, R>> {
	
	@Autowired
	protected S service;
	
	@Autowired
	protected RouteService routeService;
	
	@GetMapping(RouteService.ALL)
	public List<E> getAll() {
		
		return service.getAll();
		
	}
	
	@GetMapping(RouteService.FIND + BaseEntity.ID_MAPPING)
	public ResponseEntity<E> findById(
			@PathVariable(value = BaseEntity.ID) long id
			) throws ResourceNotFoundException {
		
		E entity = service.findById(id);
		return ResponseEntity.ok().body(entity);
		
	}
	
	@PostMapping(RouteService.CREATE)
	public E create(@Valid @RequestBody E entity) {
		
		return service.create(entity);
		
	}
	
	@PutMapping(RouteService.UPDATE + BaseEntity.ID_MAPPING)
	public ResponseEntity<E> update(
			@PathVariable(value = BaseEntity.ID) long id,
			@Valid @RequestBody E details
			) throws ResourceNotFoundException {
		
		E updatedEntity = service.update(id, details);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@DeleteMapping(RouteService.DELETE + BaseEntity.ID_MAPPING)
	public Map<String, Boolean> deleteCultureGood(
			@PathVariable(value = BaseEntity.ID) long id
			) throws Exception {
		
		service.delete(id);
		
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("deleted", true);
		return response;
		
	}
	
}
