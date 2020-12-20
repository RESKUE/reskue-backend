package reskue.backend.routing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RouteService.ERROR_ENDPOINT)
public class ErrorController {

	@RequestMapping(RouteService.AUTH_ERROR)
	public String authError() {
		return "Unauthorized access";
	}
	
}
