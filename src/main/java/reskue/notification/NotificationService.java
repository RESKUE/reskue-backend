package reskue.notification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.utility.Utility;
import reskue.culturalasset.CulturalAssetService;

/**
 * 
 * The NotificationService provides services needed by the NotificationController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@Service
public class NotificationService extends BaseService<NotificationEntity, NotificationRepository>{
	
	/**
	 * The CulturalAssetService needed to update the endangered state.
	 */
	@Autowired
	protected CulturalAssetService culturalAssetService;
	
	/**
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = NotificationController.ROUTE;
		this.routingKey = NotificationController.ROUTE;
	}
	
	/**
	 * Create a NotificationEntity.
	 * This can update the endangered state based on the type of notification created.
	 * 
	 * @param entity - the notification that should be created. This notification can not have an identifier.
	 * @return The created notification. This contains the notification's identifier.
	 */
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
