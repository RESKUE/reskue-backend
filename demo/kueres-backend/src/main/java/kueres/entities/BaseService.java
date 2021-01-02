package kueres.entities;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import kueres.eventbus.EventSubscriber;
import kueres.eventbus.RabbitMQConfiguration;

public abstract class BaseService<E extends BaseEntity<E>, R extends BaseRepository<E>> extends EventSubscriber {

	@Autowired
	protected R repository;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	protected void sendEvent(Object object) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfiguration.TOPIC_EXCHANGE,
				RabbitMQConfiguration.QUEUE, 
				object,
				m -> {
				    m.getMessageProperties().getHeaders().put("senderIdentifier", this.identifier);
				    m.getMessageProperties().getHeaders().put("senderRoutingKey", this.routingKey);
				    return m;
				});
	}
	
	public List<E> findAll() {
		
		sendEvent("findAll has been called");
		return repository.findAll();
		
	}
	
	public E findById(long id) throws ResourceNotFoundException {
		
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found: " + id));
		
	}
	
	public E create(E entity) {
		
		return repository.save(entity);
		
	}
	
	public E update(long id, E details) throws ResourceNotFoundException {
		
		E entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found: " + id));
		entity.applyPatch(details);
		final E updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}
	
	public E delete(long id) throws ResourceNotFoundException {
		
		E entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found: " + id));
		repository.delete(entity);
		return entity;
		
	}

	
}
