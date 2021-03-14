package reskue.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kueres.base.BaseController;
import kueres.utility.Utility;

@Component
public class UserInterceptor implements HandlerInterceptor, WebMvcConfigurer {
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	@Autowired
	private UserRepository repository;
	
	private String apiBaseRegex = ".*:([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
	
	@Override
	public boolean preHandle(
	  HttpServletRequest request,
	  HttpServletResponse response, 
	  Object handler) throws Exception {
		
		String url = request.getRequestURL().toString();
		if (!url.matches(this.apiBaseRegex + BaseController.API_ENDPOINT + ".*")) {
			return true;
		}
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		String username = token.getPreferredUsername();
		
		boolean exists = false;

		List<UserEntity> userEntities = this.repository.findAll();
		
		for (UserEntity userEntity : userEntities) {
			if (userEntity.getKeycloakId().equals(subject)) {
				exists = true;
				break;
			}
		}
		
		if (!exists) {
			
			UserEntity user = new UserEntity();
			user.setName(username);
			user.setKeycloakId(subject);
			
			this.repository.save(user);
			
		}
		
		return true;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(this.userInterceptor);
	}

}
