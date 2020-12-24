package reskue.backend.error;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ErrorController.ERROR_ENDPOINT)
public class ErrorController {

	public static final String ERROR_ENDPOINT = "/error";
	
	
	public static final String AUTH_ERROR = "/auth-error";
	@RequestMapping(ErrorController.AUTH_ERROR)
	public String authError() {
		return "Unauthorized access";
	}
	
}
