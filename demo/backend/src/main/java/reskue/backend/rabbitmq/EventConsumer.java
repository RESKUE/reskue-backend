package reskue.backend.rabbitmq;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private Map<String, String> subscribers = new HashMap<String, String>();
	
	public void receiveMessage(String message) {
		
		System.out.println("EventConsumer received: " + message);
		subscribers.entrySet().forEach((Entry<String, String> entry) -> {
			
			rabbitTemplate.convertAndSend(
					EventConfiguration.TOPIC_EXCHANGE,
					entry.getValue(), 
					message);
			
		});
		
	}
	
	public void subscribe(String identifier, String routingKey) {
		this.subscribers.put(identifier, routingKey);
	}
	
	public void unsubscribe(String identifier) {
		this.subscribers.remove(identifier);
	}
	
}
