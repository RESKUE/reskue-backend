package reskue.notification;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;

/**
 * 
 * The NotificationController provides API functions for NotificationEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
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

}
