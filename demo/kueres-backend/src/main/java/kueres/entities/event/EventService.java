package kueres.entities.event;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import kueres.entities.BaseService;
import kueres.entities.test.TestEntity;

@Service
public class EventService extends BaseService<EventEntity, EventRepository> {

	@Override
	@PostConstruct
	public void init() {
		this.identifier = EventController.IDENTIFIER;
		this.routingKey = EventController.ROUTING_KEY;
		this.startReceivingEvents();
	}
	
	@RabbitListener(queues = EventController.IDENTIFIER)
	public void receiveMessage(
			Message messageObject,
			@Payload String message,
			@Header("senderIdentifier") String senderIdentifier
			) throws UnsupportedEncodingException {
		System.out.println(this.identifier + " received: " + message + " from: " + senderIdentifier);
		
		EventEntity event = new EventEntity();
		event.setMessage(message);
		event.setType((int)(Math.random() * 2));
		event.setState((int)(Math.random() * 2));
		event.setSender(senderIdentifier);
		event.setSendAt(new Date());
		event.setTest(new TestEntity("test"));
		this.create(event);
		
	}

}
