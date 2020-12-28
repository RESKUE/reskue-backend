package reskue.backend.entities.demo;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import reskue.backend.entities.BaseService;

@Service
public class DemoService extends BaseService<DemoEntity, DemoRepository> {
	
	@Override
	@PostConstruct
	public void init() {
		this.identifier = DemoController.IDENTIFIER;
		this.routingKey = DemoController.ROUTING_KEY;
		this.startReceivingEvents();
	}
	
	@RabbitListener(queues = DemoController.IDENTIFIER)
	public void receiveMessage(Message message) {
		System.out.println(this.identifier + " received: " + message.toString());
	}
	
}
