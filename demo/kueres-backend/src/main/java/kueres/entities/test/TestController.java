package kueres.entities.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.entities.BaseController;

@RestController
@RequestMapping(TestController.ROUTE)
public class TestController extends BaseController<TestEntity, TestRepository, TestService> {

	public static final String IDENTIFIER = "TestController";
	public static final String ROUTE = BaseController.API_ENDPOINT + "/test";
	public static final String ROUTING_KEY = "test";
	
}
