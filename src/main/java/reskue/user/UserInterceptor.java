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
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.utility.Utility;

@Component
public class UserInterceptor implements HandlerInterceptor, WebMvcConfigurer {
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public boolean preHandle(
	  HttpServletRequest request,
	  HttpServletResponse response, 
	  Object handler) throws Exception {
		
		
		
		if (!request.getRequestURL().toString().contains(BaseController.API_ENDPOINT)) {
			return true;
		}
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		String username = token.getPreferredUsername();
		
		boolean exists = false;

		EntitySpecification<UserEntity> specification = new EntitySpecification<UserEntity>();
		specification.add(new SearchCriteria("keycloakId~" + subject));
		List<UserEntity> userEntities = this.repository.findAll(specification);
		
		Utility.LOG.info("{} exists: {}", username, exists);
		
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
