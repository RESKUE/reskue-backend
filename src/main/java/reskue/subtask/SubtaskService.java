package reskue.subtask;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.utility.Utility;

@Service
public class SubtaskService extends BaseService<SubtaskEntity, SubtaskRepository>{

	@Override
	@PostConstruct
	public void init() {
		
	}
	
	public SubtaskEntity changeState(long id, int state) {
		
		Utility.LOG.trace("SubtaskService.changeState called.");
		
		SubtaskEntity entity = this.findById(id);
		
		entity.setState(state);
		
		final SubtaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("SubtaskService.changeState", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}
	
}
