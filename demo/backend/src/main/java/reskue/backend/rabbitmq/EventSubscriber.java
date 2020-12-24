package reskue.backend.rabbitmq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EventSubscriber {

	@Autowired
	private AmqpAdmin amqpAdmin;
	
	@Autowired
	private EventConsumer eventConsumer;
	
	private Queue queue;
	private Binding binding;
	
	protected String identifier;
	protected String routingKey;
	
	public abstract void init();
	
	protected void startReceivingEvents() {
		
		this.queue = new Queue(this.identifier, false, false, false); //ToDo: durable - should event queues survive server restarts
		this.binding = new Binding(this.identifier, Binding.DestinationType.QUEUE, EventConfiguration.TOPIC_EXCHANGE, this.routingKey, null);
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareBinding(binding);
		
		this.eventConsumer.subscribe(this.identifier, this.routingKey);
		
	}
	
	protected void stopReceivingEvents() {
		
		this.eventConsumer.unsubscribe(this.identifier);
		
		amqpAdmin.removeBinding(this.binding);
		amqpAdmin.deleteQueue(this.queue.getName());
		
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public String getRoutingKey() {
		return this.routingKey;
	}
	
}
