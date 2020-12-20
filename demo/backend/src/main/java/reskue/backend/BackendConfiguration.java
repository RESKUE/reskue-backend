package reskue.backend;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import reskue.backend.interceptors.AuthInterceptor;
import reskue.backend.routing.Route;
import reskue.backend.routing.RouteService;

@Configuration
public class BackendConfiguration implements WebMvcConfigurer {

	@Autowired
	private AuthInterceptor authInterceptor;
	
	@Autowired
	private RouteService routeService;
	
	@PostConstruct
	public void init() {
		
		Route apiEndpoint = new Route(RouteService.API_ENDPOINT, true);
		routeService.configureRoute(apiEndpoint);
		
		Route eventEndpoint = new Route(RouteService.EVENT_ENDPOINT, true);
		routeService.configureRoute(eventEndpoint);
		
		Route errorEndpoint = new Route(RouteService.ERROR_ENDPOINT, false);
		routeService.configureRoute(errorEndpoint);
		
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(authInterceptor);
		
	}
	
}
