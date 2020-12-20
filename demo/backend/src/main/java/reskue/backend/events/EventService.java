package reskue.backend.events;

import java.util.List;

import org.springframework.stereotype.Service;

import reskue.backend.entities.BaseService;

@Service
public class EventService extends BaseService<Event, EventRepository> {

	public List<Event> findByType(String type) {
		
		return repository.findEventByType(type);
		
	}
	
}
