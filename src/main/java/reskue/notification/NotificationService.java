package reskue.notification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.utility.Utility;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.culturalasset.CulturalAssetService;

/**
 * 
 * The NotificationService provides services needed by the NotificationController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
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
			
			CulturalAssetEntity culturalAsset = this.culturalAssetService.findById(entity.getEntity().getId());
			
			// if there is a cultural asset specified and the notification is an alarm this marks the cultural asset as endangered
			if (entity.getType() == NotificationType.ALARM.type) {
				
				culturalAssetService.updateIsEndangered(culturalAsset, 1);
				
			}
			
			// if there is a cultural asset specified and the notification is an alarm over this marks the cultural asset as endangered
			if (entity.getType() == NotificationType.ALARM_OVER.type) {
				
				culturalAssetService.updateIsEndangered(culturalAsset, 0);	
				
			}
			
		}
		
		NotificationEntity savedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("NotificationService.create", EventType.CREATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(savedEntity));
		
		return savedEntity;
		
	}
	
}
