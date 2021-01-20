package kueres.entities.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.entities.BaseController;

@RestController
@RequestMapping(EventController.ROUTE)
public class EventController extends BaseController<EventEntity, EventRepository, EventService>{

	public static final String IDENTIFIER = "EventController";
	public static final String ROUTE = BaseController.API_ENDPOINT + "/event";
	public static final String ROUTING_KEY = "event";
	
	@Autowired
	protected EventService service;
	
	
	
}

@RestController
@RequestMapping("/api/event")
class TestingController {
	
}