package reskue.backend.events;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reskue.backend.entities.BaseController;
import reskue.backend.routing.RouteService;

@RestController
@RequestMapping(RouteService.EVENT_ENDPOINT)
public class EventController extends BaseController<Event, EventRepository, EventService> {

	@GetMapping(Event.TYPE_MAPPING)
	public List<Event> getEventByType(
			@PathVariable(value = Event.TYPE) String type
			) {
		
		return service.findByType(type);
		
	}
	
}
