package kueres.auth;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kueres.utility.Utility;

@Component
public class UserInterceptor implements HandlerInterceptor, WebMvcConfigurer {

	private int cnt = 0;
	private Map<String, Integer> users = new HashMap<String, Integer>();
	
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler
			) throws Exception {
		
		Principal principal = request.getUserPrincipal();
		String userToken = principal.getName();
		
		if (users.containsKey(userToken)) {
			Utility.LOG.info("user exists! id: {}, token: {}", users.get(userToken), userToken);
		} else {
			users.put(userToken, cnt);
			cnt++;
			Utility.LOG.info("user created! id: {}, token: {}", users.get(userToken), userToken);
		}
		
		return true;
		
	}
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor);
	}
	
}
