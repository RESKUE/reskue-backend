package reskue.backend.entities.event;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reskue.backend.entities.BaseController;
import reskue.backend.entities.BaseEntity;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + EventController.ROUTE)
public class EventController {

	public static final String IDENTIFIER = "EventController";
	public static final String ROUTE = "/event";
	public static final String ROUTING_KEY = "event";
	
	@Autowired
	protected EventService service;
	
	@GetMapping()
	@RolesAllowed({"administrator", "helper"})
	public List<EventEntity> findAll() {
		
		return service.findAll();
		
	}
	
	@GetMapping(BaseEntity.ID_MAPPING)
	@RolesAllowed({"administrator", "helper"})
	public ResponseEntity<EventEntity> findById(
			@PathVariable(value = BaseEntity.ID) long id
			) throws ResourceNotFoundException {
		
		EventEntity entity = service.findById(id);
		return ResponseEntity.ok().body(entity);
		
	}
	
}
