package reskue.backend.entities.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reskue.backend.entities.BaseController;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + TestController.ROUTE)
public class TestController {

	public static final String IDENTIFIER = "TestController";
	public static final String ROUTE = "/test";
	public static final String ROUTING_KEY = "test";
	
}
