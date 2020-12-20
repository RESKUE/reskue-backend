package reskue.backend.entities.culturegoods;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reskue.backend.entities.BaseController;
import reskue.backend.routing.RouteService;

@RestController
@RequestMapping(RouteService.API_ENDPOINT + RouteService.CULTURE_GOOD)
public class CultureGoodController extends BaseController<CultureGood, CultureGoodRepository, CultureGoodService> {

	
	@GetMapping("/test")
	public List<CultureGood> test() {
		 return service.findByName("Test");
	}
	
}
