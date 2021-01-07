package kueres.entities.event;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.entities.BaseController;
import kueres.entities.BaseEntity;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
import kueres.utility.Utility;

@RestController
@RequestMapping(EventController.ROUTE)
public class EventController {

	public static final String IDENTIFIER = "EventController";
	public static final String ROUTE = BaseController.API_ENDPOINT + "/event";
	public static final String ROUTING_KEY = "event";
	
	@Autowired
	protected EventService service;
	
	@GetMapping
	@RolesAllowed({"administrator", "helper"})
	public Page<EventEntity> findAll(
			@RequestParam Optional<String> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		EntitySpecification<EventEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<EventEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}
		
		Sort sorting = Sort.unsorted();
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
			Utility.LOG.info("sorting: {}", sorting);
		}
		
		Pageable pagination = Pageable.unpaged();
		if (page.isPresent() && size.isPresent()) {
			pagination = PageRequest.of(page.get(), size.get(), sorting);
		}
		
		return service.findAllImproved(specification, pagination);
		
	}
	
	@GetMapping("/findById/{id}")
	@RolesAllowed({"administrator", "helper"})
	public ResponseEntity<EventEntity> findById(
			@PathVariable(value = BaseEntity.ID) long id
			) throws ResourceNotFoundException {
		
		EventEntity entity = service.findById(id);
		return ResponseEntity.ok().body(entity);
		
	}
	
	@GetMapping("/findByType/{type}")
	@RolesAllowed({"administrator", "helper"})
	public int findByType(
			@PathVariable(value = "type") int type
			) throws ResourceNotFoundException {
		
		return type;
		
	}
	
}
