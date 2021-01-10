package kueres.entities.test;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.entities.BaseService;

@Service
public class TestService extends BaseService<TestEntity, TestRepository> {

	@Override
	@PostConstruct
	public void init() {
		this.identifier = TestController.IDENTIFIER;
		this.routingKey = TestController.ROUTING_KEY;
		this.startReceivingEvents();
	}

}
