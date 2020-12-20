package reskue.backend.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import reskue.backend.routing.RouteService;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	@Autowired
	RouteService routeService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String path = request.getRequestURI();
		if (routeService.isAuthorizationRequiredForPath(path)) {
			
			boolean authorized = false;
			String authHeader = request.getHeader("Authorization");

			if (authHeader != null) {
				authorized = authHeader.contentEquals("Bearer abc");
			}

			if (!authorized) {
				response.sendRedirect(RouteService.ERROR_ENDPOINT + RouteService.AUTH_ERROR);
			}

			return authorized;
			
		}
		
		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {

	}

}