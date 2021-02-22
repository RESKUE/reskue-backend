package reskue.notification;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + NotificationController.ROUTE)
public class NotificationController
		extends BaseController<NotificationEntity, NotificationRepository, NotificationService> {

	public static final String ROUTE = "/notification";

}
