package reskue.backend.entities.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reskue.backend.entities.BaseController;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + DemoController.ROUTE)
public class DemoController extends BaseController<DemoEntity, DemoRepository, DemoService> {

	public static final String IDENTIFIER = "DemoController";
	public static final String ROUTE = "/demo";
	public static final String ROUTING_KEY = "demo";
	
}
