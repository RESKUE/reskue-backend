package reskue.notification;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.utility.Utility;

@Service
public class NotificationService extends BaseService<NotificationEntity, NotificationRepository>{

	@Override
	@PostConstruct
	public void init() {
		this.identifier = NotificationController.ROUTE;
		this.routingKey = NotificationController.ROUTE;
	}

	@Override
	public NotificationEntity create(NotificationEntity entity) {
		
		Utility.LOG.trace("NotificationService.create called.");
		
		if (entity.getType() == NotificationType.ALARM.type) {
			entity.getEntity().setIsEndangered(1);
		}
		
		NotificationEntity savedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("NotificationService.create", EventType.CREATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(savedEntity));
		
		return savedEntity;
		
	}
	
}
