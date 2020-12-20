package reskue.backend.routing;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RouteService {

	//End points
	public static final String API_ENDPOINT = "/api";
	public static final String EVENT_ENDPOINT = "/event";
	public static final String ERROR_ENDPOINT = "/error";
	
	//Standard methods
	public static final String ALL = "/all";
	public static final String FIND = "/find";
	public static final String CREATE = "/create";
	public static final String UPDATE = "/update";
	public static final String DELETE = "/delete";
	
	//Error handlers
	public static final String AUTH_ERROR = "/auth";
	
	//Entities
	public static final String CULTURE_GOOD = "/cultureGood";
	
	private Map<String, Route> routeOptions = new HashMap<String, Route>();
	
	public void configureRoute(Route route) {
		
		routeOptions.put(route.getIdentifier(), route);
		
	}
	
	public void deleteRoute(String identifier) throws UnsupportedOperationException {
		
		if (!routeOptions.containsKey(identifier)) {
			throw new UnsupportedOperationException("No route configured with that identifier.");
		}
		
		routeOptions.remove(identifier);
		
	}
	
	public boolean isAuthorizationRequiredForPath(String path) {
		
		boolean authorizationRequired = false;
		String[] segements = path.split("/");
		for (int i = 0; i < segements.length && !authorizationRequired; i++) {
			
			if (routeOptions.containsKey("/" + segements[i])) {
				authorizationRequired = routeOptions.get("/" + segements[i]).needsAuthorization();
			}
			
		}
		return authorizationRequired;
		
	}
	
}
