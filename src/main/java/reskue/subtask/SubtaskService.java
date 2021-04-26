package reskue.subtask;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.utility.Utility;

/**
 * 
 * The SubtaskService provides services needed by the SubtaskController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

@Service
public class SubtaskService extends BaseService<SubtaskEntity, SubtaskRepository>{
	
	/**
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = SubtaskController.ROUTE;
		this.routingKey = SubtaskController.ROUTE;
	}
	
	/**
	 * Changes the state of a subtask.
	 * 
	 * @param id - the subtask's identifier.
	 * @param state - the new state of the subtask.
	 * @return The subtask after the state was changed.
	 */
	public SubtaskEntity changeState(Long id, int state) {
		
		Utility.LOG.trace("SubtaskService.changeState called.");
		
		SubtaskEntity entity = this.findById(id);
		
		entity.setState(state);
		
		final SubtaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("SubtaskService.changeState", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}
	
}
