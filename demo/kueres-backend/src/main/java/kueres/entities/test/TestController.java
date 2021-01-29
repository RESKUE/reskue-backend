package kueres.entities.test;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.entities.BaseController;

@RestController
@RequestMapping(TestController.ROUTE)
public class TestController extends BaseController<TestEntity, TestRepository, TestService> {

	public static final String IDENTIFIER = "TestController";
	public static final String ROUTE = "/api/test";
	public static final String ROUTING_KEY = "test";
	
	@Autowired
	private LocationServiceInterface locationService;
	
	@Override
	@GetMapping()
	@RolesAllowed({"administrator", "helper"})
	public List<TestEntity> findAll(
			@RequestParam Optional<String> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		List<TestEntity> list = new ArrayList<TestEntity>();
		TestEntity test = new TestEntity();
		test.setName(locationService.get());
		list.add(test);
		return list;
		
	}
	
}
