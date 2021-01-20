package kueres.eventbus;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import kueres.media.MediaService;
import kueres.utility.Utility;

@Component
public class EventConsumer implements MessageListener {
	
	private OnMessageExecutor onMessageExecutor = (Message message, Map<String, String> subscribers) -> defaultOnMessageExecutor(message, subscribers);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private Map<String, String> subscribers = new HashMap<String, String>();
	
	public void subscribe(String identifier, String routingKey) {
		this.subscribers.put(identifier, routingKey);
	}
	
	public void unsubscribe(String identifier) {
		this.subscribers.remove(identifier);
	}

	@Override
	public void onMessage(Message message) {
		
		this.onMessageExecutor.execute(message, this.subscribers);
		
	}
	
	public void setOnMessageExecutor(OnMessageExecutor onMessageExecutor) {
		this.onMessageExecutor = onMessageExecutor;
	}
	
	private void defaultOnMessageExecutor(Message message, Map<String, String> subscribers) {
		
		System.out.println("EventConsumer received: " + message.toString());
		subscribers.entrySet().forEach((Entry<String, String> entry) -> {
			
			rabbitTemplate.convertAndSend(
					RabbitMQConfiguration.TOPIC_EXCHANGE,
					entry.getValue(), 
					message);
			
		});
		
	}
	
	@Bean
	public boolean testOnMessageExecutor(EventConsumer consumer) {
		OnMessageExecutor onMessageExecutor = (Message message, Map<String, String> subscribers) -> {
			Utility.LOG.info("custom onMessageExecutor called");
			System.out.println("EventConsumer received: " + message.toString());
			subscribers.entrySet().forEach((Entry<String, String> entry) -> {
				
				rabbitTemplate.convertAndSend(
						RabbitMQConfiguration.TOPIC_EXCHANGE,
						entry.getValue(), 
						message);
				
			});
		};
		consumer.setOnMessageExecutor(onMessageExecutor);
		return true;
	}
	
}