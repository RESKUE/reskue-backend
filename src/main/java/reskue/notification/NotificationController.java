package reskue.notification;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.utility.Utility;
import reskue.user.UserEntity;
import reskue.user.UserService;

/**
 * 
 * The NotificationController provides API functions for NotificationEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + NotificationController.ROUTE)
public class NotificationController
		extends BaseController<NotificationEntity, NotificationRepository, NotificationService> {
	
	/**
	 * The API route for NotificationEntities.
	 */
	public static final String ROUTE = "/notification";
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/autoSender")
	@RolesAllowed("administrator")
	public ResponseEntity<NotificationEntity> create(
			@Valid @RequestBody NotificationEntity entity,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		Utility.LOG.trace("NotificationController.create called.");
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		UserEntity sender = userService.me(subject);
		entity.setSender(sender);
		
		NotificationEntity createdEntity = service.create(entity);
		return ResponseEntity.ok().body(createdEntity);
		
	}

}
