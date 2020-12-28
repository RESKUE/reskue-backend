package reskue.backend.entities.test;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import reskue.backend.entities.BaseService;

@Service
public class TestService extends BaseService<TestEntity, TestRepository> {

	@Override
	@PostConstruct
	public void init() {
		this.identifier = TestController.IDENTIFIER;
		this.routingKey = TestController.ROUTING_KEY;
		this.startReceivingEvents();
	}
	
	@RabbitListener(queues = TestController.IDENTIFIER)
	public void receiveMessage(Message message) {
		System.out.println(this.identifier + " received: " + message.toString());
	}
	
}
