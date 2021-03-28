package reskue.notification;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.base.BaseEntity;
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
	
	/**
	 * The UserService needed to get the user for the automatic notification sender.
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * Creates a notification and automatically sets the sender to the user that send the create request.
	 * 
	 * @param request - the request send by the user.
	 * @param response - the response.
	 * @return - the created notification with the sender.
	 * 
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@PostMapping("/autoSender")
	@RolesAllowed("administrator")
	public ResponseEntity<NotificationEntity> createAutoSender(
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Utility.LOG.trace("NotificationController.create called.");
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		UserEntity sender = userService.me(subject);
		
		String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		Class<NotificationEntity> entityClass = this.service.getEntityClass();
		NotificationEntity entity = entityClass.getDeclaredConstructor().newInstance();
		entity = BaseEntity.createEntityFromJSON(body, entity.getUpdateableFields(), entityClass);
		entity.setSender(sender);
		
		NotificationEntity created = this.service.create(entity);
		return ResponseEntity.ok().body(created);
		
	}

}
