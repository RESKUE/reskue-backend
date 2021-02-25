package reskue.notification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.utility.Utility;
import reskue.culturalasset.CulturalAssetService;

@Service
public class NotificationService extends BaseService<NotificationEntity, NotificationRepository>{

	@Autowired
	protected CulturalAssetService culturalAssetService;
	
	@Override
	@PostConstruct
	public void init() {
		this.identifier = NotificationController.ROUTE;
		this.routingKey = NotificationController.ROUTE;
	}

	@Override
	public NotificationEntity create(NotificationEntity entity) {
		
		Utility.LOG.trace("NotificationService.create called.");
		
		if (entity.getEntity() != null) {
			
			if (entity.getType() == NotificationType.ALARM.type) {
				
				culturalAssetService.updateIsEndangered(entity.getEntity(), 1);
				
			}
			if (entity.getType() == NotificationType.ALARM_OVER.type) {
				
				culturalAssetService.updateIsEndangered(entity.getEntity(), 0);	
				
			}
			
		}
		
		NotificationEntity savedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("NotificationService.create", EventType.CREATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(savedEntity));
		
		return savedEntity;
		
	}
	
}
