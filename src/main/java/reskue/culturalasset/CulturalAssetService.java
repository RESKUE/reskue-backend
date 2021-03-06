package reskue.culturalasset;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kueres.base.BaseEntity;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.location.LocationService;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;
import reskue.ReskueService;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.task.TaskRepository;

/**
 * 
 * The CulturalAssetService provides services needed by the
 * CulturalAssetController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

@Service
public class CulturalAssetService extends ReskueService<CulturalAssetEntity, CulturalAssetRepository> {

	/**
	 * The LocationService needed to save cultural assets in the FROST server.
	 */
	@Autowired
	protected LocationService locationService;

	/**
	 * The TaskRepository of the TaskEntity needed to save TaskEntitys after
	 * updating them.
	 */
	@Autowired
	protected TaskRepository taskRepository;

	/**
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = CulturalAssetController.ROUTE;
		this.routingKey = CulturalAssetController.ROUTE;
	}

	/**
	 * Create a CulturalAssetEntity.
	 * 
	 * @param entity - the cultural asset that should be created. This cultural
	 *               asset can not have an identifier.
	 * @return The created cultural asset. This contains the cultural asset's
	 *         identifier.
	 */
	@Override
	public CulturalAssetEntity create(CulturalAssetEntity entity) {

		Utility.LOG.trace("CulturalAssetService.create called.");
		
		// calculates an address if only coordinates are given
		if (entity.getLongitude() != null && entity.getLatitude() != null) {
			
			if (entity.getAddress() == null) {
				
				entity.setAddress(locationService
						.coordinatesToAddress(new double[] { entity.getLongitude(), entity.getLatitude() }));
				
			}
			
		// calculates coordinates if only an address is given
		} else if (entity.getAddress() != null) {
			
			if (entity.getLongitude() == null && entity.getLatitude() == null) {
				
				double[] updatedCoordinates = locationService.addressToCoordinates(entity.getAddress());
				entity.setLongitude(updatedCoordinates[0]);
				entity.setLatitude(updatedCoordinates[1]);
				
			}
			
		}
		
		// if coordinates exist adds the cultural asset to the location database
		if (entity.getLongitude() != null && entity.getLatitude() != null) {
			
			entity.setLocationId(locationService.addPOI(entity.getName(),
					new double[] { entity.getLongitude(), entity.getLatitude() }));
			
		}

		CulturalAssetEntity savedEntity = this.repository.save(entity);
		
		// if after saving a parent exists adds the relation
		if (savedEntity.getCulturalAssetParent() != null) {
			
			this.setCulturalAssetParent(savedEntity.getId(), savedEntity.getCulturalAssetParent().getId());
			
		}
		
		// if after saving children exist add the relations
		if (savedEntity.getCulturalAssetChildren() != null || !entity.getCulturalAssetChildren().isEmpty()) {
			
			savedEntity.getCulturalAssetChildren().forEach((CulturalAssetEntity child) -> {
				this.addCulturalAssetChild(savedEntity.getId(), child.getId());
			});

		}
		
		// ensures that the endangered state is set correctly
		this.updateIsEndangered(savedEntity, savedEntity.getIsEndangered());

		EventConsumer.sendEvent("CulturalAssetService.create", EventType.CREATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(savedEntity));

		return savedEntity;

	}

	/**
	 * Update a cultural asset. Fields that are not populated in the updated data
	 * will not be changed.
	 * 
	 * @param id      - the cultural asset's identifier.
	 * @param detailsJSON - the updated data in JSON format.
	 * @return The updated cultural asset.
	 * @throws ResourceNotFoundException if there is no cultural asset with the
	 *                                   specified identifier.
	 * @throws JsonProcessingException if the JSON string can not be processed
	 * @throws SecurityException if the JSON string can not be processed
	 * @throws NoSuchMethodException if the JSON string can not be processed
	 * @throws InvocationTargetException if the JSON string can not be processed
	 * @throws IllegalArgumentException if the JSON string can not be processed
	 * @throws IllegalAccessException if the JSON string can not be processed
	 * @throws InstantiationException if the JSON string can not be processed
	 * @throws JsonMappingException if the JSON string can not be processed
	 */
	@Override
	public CulturalAssetEntity update(Long id, String detailsJSON) throws ResourceNotFoundException, JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {

		Utility.LOG.trace("CulturalAssetService.update called.");

		CulturalAssetEntity details = CulturalAssetEntity.createEntityFromJSON(detailsJSON, new CulturalAssetEntity().getUpdateableFields(), CulturalAssetEntity.class);
		
		CulturalAssetEntity entity = this.findById(id);

		entity.applyPatch(detailsJSON);
		
		// removes the parent and sets it again after saving to ensure that there is no bad interaction between the old and the new parent if they are different
		if (BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.CULTURAL_ASSET_PARENT)) {
			
			this.removeCulturalAssetParent(entity.getId());
			
		}
		
		// removes the children and sets them again after saving to ensure that there is no bad interaction between the old and the new children if they are different
		if (BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.CULTURAL_ASSET_CHILDREN)) {
			
			for (Iterator<CulturalAssetEntity> iterator = entity.getCulturalAssetChildren().iterator(); iterator.hasNext();) {
				CulturalAssetEntity child = iterator.next();
				iterator.remove();
				this.removeCulturalAssetChild(id, child.getId());
			}
			
		}
		
		// if coordinates exist this calculates a new address even if an adress already existed because the coordinates take priority due to their importance in calculations
		if (BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.LONGITUDE) || BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.LATITUDE)) {
			
			entity.setAddress(locationService.coordinatesToAddress(new double[] { details.getLongitude(), details.getLatitude()}));
			
		// if only an address exists this calculates new coordinates
		} else if (BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.ADDRESS)) {
			
			double[] coordinates = locationService.addressToCoordinates(details.getAddress());
			entity.setLongitude(coordinates[0]);
			entity.setLatitude(coordinates[1]);
			
		}

		final CulturalAssetEntity savedEntity = this.repository.save(entity);
		
		// sets the updated parent if it exists
		if (BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.CULTURAL_ASSET_PARENT)) {
			
			this.setCulturalAssetParent(savedEntity.getId(), savedEntity.getCulturalAssetParent().getId());
			
		}
		
		// adds the updated children if they exist 
		if (BaseEntity.containsFields(detailsJSON, CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) || !savedEntity.getCulturalAssetChildren().isEmpty()) {
			
			savedEntity.getCulturalAssetChildren().forEach((CulturalAssetEntity child) -> {
				this.addCulturalAssetChild(savedEntity.getId(), child.getId());
			});
			
		}
		
		this.updateIsEndangered(savedEntity, savedEntity.getIsEndangered());

		EventConsumer.sendEvent("CulturalAssetService.update", EventType.UPDATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(savedEntity));

		return savedEntity;

	}

	/**
	 * Delete a cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @return The cultural asset that was deleted.
	 * @throws ResourceNotFoundException if there is no cultural asset with the
	 *                                   specified identifier.
	 */
	@Override
	public CulturalAssetEntity delete(Long id) throws ResourceNotFoundException {

		Utility.LOG.trace("CulturalAssetService.delete called.");

		CulturalAssetEntity entity = this.repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		// ensures that the parent relation gets removed
		if (entity.getCulturalAssetParent() != null) this.removeCulturalAssetParent(id);
		
		// ensures that the children relations get removed
		for (Iterator<CulturalAssetEntity> iterator = entity.getCulturalAssetChildren().iterator(); iterator.hasNext();) {
			
			CulturalAssetEntity child = iterator.next();
			iterator.remove();
			this.removeCulturalAssetChild(id, child.getId());
			
		}
		
		// ensures that the cultural asset is removed from the location database
		if (entity.getLocationId() != null) {
			
			locationService.removePOI(entity.getLocationId());
			
		}
		
		// ensures that all related media is deleted properly
		for (Iterator<MediaEntity> iterator = entity.getMedia().iterator(); iterator.hasNext();) {
			MediaEntity media = iterator.next();
			iterator.remove();
			mediaService.delete(media.getId());
		}
		
		this.repository.delete(entity);

		EventConsumer.sendEvent("CulturalAssetService.delete", EventType.DELETE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(entity));

		return entity;

	}

	/**
	 * Get all cultural assets in a radius around a given middle point.
	 * 
	 * @param radius        - the radius of the search.
	 * @param longitude     - the longitude of the middle of the search.
	 * @param latitude      - the latitude of the middle of the search.
	 * @param specification - filter for the result.
	 * @param pageable      - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<CulturalAssetEntity> findInRadius(double radius, double longitude, double latitude,
			EntitySpecification<CulturalAssetEntity> specification, Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.findInRadius called.");

		List<String> entityIds = locationService.findInRadius(radius, new double[] { longitude, latitude });
		List<CulturalAssetEntity> entities = entityIds.stream().map(this.repository::findByLocationId)
				.collect(Collectors.toList());

		if (specification != null) {

			entities = entities.stream().filter(specification.toPredicate(CulturalAssetEntity.class)).collect(Collectors.toList());
			
		}

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(entities, pageable, entities.size());

		EventConsumer.sendEvent("CulturalAssetService.findInRadius", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}
	
	/**
	 * Get the distance between a point and the cultural asset.
	 * 
	 * @param id        - the cultural asset's identifier.
	 * @param longitude - the longitude of the point.
	 * @param latitude  - the latitude of the point.
	 * @return The result as a double.
	 */
	public double getDistance(Long id, double longitude, double latitude) {

		Utility.LOG.trace("CulturalAssetService.getDistance called.");

		CulturalAssetEntity entity = this.findById(id);

		double[] entityLocation = new double[] { entity.getLongitude(), entity.getLatitude() };

		double distance = locationService.calculateDistance(entityLocation, new double[] { longitude, latitude });

		EventConsumer.sendEvent("CulturalAssetService.getDistance", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(distance));

		return distance;

	}

	/**
	 * Get all tasks of the cultural asset.
	 * 
	 * @param id            - the cultural asset's identifier.
	 * @param specification - filter for the result.
	 * @param pageable      - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<TaskEntity> getAllTasks(Long id, EntitySpecification<TaskEntity> specification, Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.getAllTasks called.");

		CulturalAssetEntity entity = this.findById(id);
		List<TaskEntity> tasks = entity.getTasks();

		if (specification != null) {

			tasks = tasks.stream().filter(specification.toPredicate(TaskEntity.class)).collect(Collectors.toList());
			
		}

		Page<TaskEntity> page = new PageImpl<TaskEntity>(tasks, pageable, tasks.size());

		EventConsumer.sendEvent("CulturalAssetService.getAllTasks", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}
	
	/**
	 * Get all comments of the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<CommentEntity> getAllComments(Long id, EntitySpecification<CommentEntity> specification,
			Pageable pageable) {
		
		Utility.LOG.trace("CulturalAssetService.getAllComments called.");
		
		CulturalAssetEntity entity = this.findById(id);

		List<CommentEntity> comments = entity.getComments();

		if (specification != null) {	
			
			comments = comments.stream().filter(specification.toPredicate(CommentEntity.class)).collect(Collectors.toList());
			
		}

		Page<CommentEntity> page = new PageImpl<CommentEntity>(comments, pageable, comments.size());
		
		EventConsumer.sendEvent("CulturalAssetService.getAllComments", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}

	/**
	 * Get all children of the cultural asset.
	 * 
	 * @param id            - the cultural asset's identifier.
	 * @param specification - filter for the result.
	 * @param pageable      - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<CulturalAssetEntity> getAllChildren(Long id, EntitySpecification<CulturalAssetEntity> specification,
			Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.getAllChildren called.");

		CulturalAssetEntity entity = this.findById(id);
		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();

		if (specification != null) {

			children = children.stream().filter(specification.toPredicate(CulturalAssetEntity.class)).collect(Collectors.toList());
			
		}

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(children, pageable, children.size());

		EventConsumer.sendEvent("CulturalAssetService.getAllChildren", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	/**
	 * Get all children of the cultural asset.
	 * 
	 * @param id            - the cultural asset's identifier.
	 * @param specification - filter for the result.
	 * @param pageable      - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<NotificationEntity> getAllNotifications(Long id, EntitySpecification<NotificationEntity> specification,
			Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.getAllNotifications called.");

		CulturalAssetEntity entity = this.findById(id);
		List<NotificationEntity> notifications = entity.getNotifications();

		if (specification != null) {

			notifications = notifications.stream().filter(specification.toPredicate(NotificationEntity.class)).collect(Collectors.toList());
			
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(notifications, pageable, notifications.size());

		EventConsumer.sendEvent("CulturalAssetService.getAllNotifications", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	/**
	 * Adds a child to the cultural asset. If the child violates either the hierarchy height or creates a loop it is simply not added and doesn't throw an error.
	 * This is to ensure that operations with many relations don't fail based on one wrong connection.
	 * 
	 * @param id      - the cultural asset's identifier.
	 * @param childId - the child's identifier.
	 * @return The cultural asset after the child was added.
	 */
	public CulturalAssetEntity addCulturalAssetChild(Long id, Long childId) {

		Utility.LOG.trace("CulturalAssetService.addCulturalAssetChild called.");

		CulturalAssetEntity parent = this.findById(id);
		CulturalAssetEntity child = this.findById(childId);
		CulturalAssetEntity updatedEntity = parent;

		this.addConnection(child, parent);

		if (this.testLoopError(child) || this.testHeightError(child)) {

			this.removeConnection(child, parent);

		} else {

			updatedEntity = this.repository.save(parent);
			this.repository.save(child);

		}

		EventConsumer.sendEvent("CulturalAssetService.addCulturalAssetChild", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	/**
	 * Removes a child to the cultural asset.
	 * 
	 * @param id      - the cultural asset's identifier.
	 * @param childId - the child's identifier.
	 * @return The cultural asset after the child was removed.
	 */
	public CulturalAssetEntity removeCulturalAssetChild(Long id, Long childId) {

		Utility.LOG.trace("CulturalAssetService.removeCulturalAssetChild called.");

		CulturalAssetEntity parent = this.findById(id);
		CulturalAssetEntity child = this.findById(childId);

		this.removeConnection(child, parent);

		final CulturalAssetEntity updatedEntity = this.repository.save(parent);

		this.repository.save(child);

		EventConsumer.sendEvent("CulturalAssetService.removeCulturalAssetChild", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	/**
	 * Sets the parent of the cultural asset. If the parent violates either the hierarchy height or creates a loop it is simply not added and doesn't throw an error.
	 * This is to ensure that operations with many relations don't fail based on one wrong connection.
	 * 
	 * @param id       - the cultural asset's identifier.
	 * @param parentId - the parent's identifier.
	 * @return The cultural asset after the parent was set.
	 */
	public CulturalAssetEntity setCulturalAssetParent(Long id, Long parentId) {

		Utility.LOG.trace("CulturalAssetService.setCulturalAssetParent called.");
		
		CulturalAssetEntity child = this.findById(id);
		CulturalAssetEntity parent = this.findById(parentId);
		CulturalAssetEntity updatedEntity = child;
		
		this.addConnection(child, parent);

		if (this.testLoopError(child) || this.testHeightError(child)) {

			this.removeConnection(child, parent);

		} else {

			updatedEntity = this.repository.save(child);
			this.repository.save(parent);

		}

		EventConsumer.sendEvent("CulturalAssetService.setCulturalAssetParent", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	/**
	 * Removes the parent of the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @return The cultural asset after the parent was removed.
	 */
	public CulturalAssetEntity removeCulturalAssetParent(Long id) {

		Utility.LOG.trace("CulturalAssetService.removeCulturalAssetParent called.");
		
		CulturalAssetEntity child = this.findById(id);
		CulturalAssetEntity parent = this.findById(child.getCulturalAssetParent().getId());
		
		this.removeConnection(child, parent);

		final CulturalAssetEntity updatedEntity = this.repository.save(child);

		this.repository.save(parent);

		EventConsumer.sendEvent("CulturalAssetService.removeCulturalAssetParent", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	/**
	 * Adds a connection between a parent and a child in the hierarchy of cultural
	 * assets.
	 * 
	 * @param child  - the child.
	 * @param parent - the parent.
	 */
	private void addConnection(CulturalAssetEntity child, CulturalAssetEntity parent) {

		Utility.LOG.trace("CulturalAssetService.addConnection called.");		
		
		List<CulturalAssetEntity> newChildren = parent.getCulturalAssetChildren();
		
		// if the new child is already a child
		if (!newChildren.contains(child)) {
			
			newChildren.add(child);
			parent.setCulturalAssetChildren(newChildren);
			child.setCulturalAssetParent(parent);
			this.updateLevels(child, parent.getLevel() + 1);
			
		}

	}

	/**
	 * Removes a connection between a parent and a child in the hierarchy of
	 * cultural assets.
	 * 
	 * @param child  - the child.
	 * @param parent - the parent.
	 */
	private void removeConnection(CulturalAssetEntity child, CulturalAssetEntity parent) {

		Utility.LOG.trace("CulturalAssetService.removeConnection called.");

		List<CulturalAssetEntity> newChildren = parent.getCulturalAssetChildren();
		
		// if the child is actually a child
		if (newChildren.contains(child)) {
			
			newChildren.remove(child);
			parent.setCulturalAssetChildren(newChildren);
			child.setCulturalAssetParent(null);
			this.updateLevels(child, 0);
			
		}

	}

	/**
	 * Checks if a cultural asset is part of a loop in the hierarchy of cultural
	 * assets.
	 * 
	 * @param entity - the cultural asset.
	 * @return If there is an error, true if yes and false if no.
	 */
	private boolean testLoopError(CulturalAssetEntity entity) {

		CulturalAssetEntity parent = entity.getCulturalAssetParent();

		for (int i = 0; i < 4; i++) {

			if (parent == null) {
				
				return false;
				
			} else {
				
				parent = parent.getCulturalAssetParent();
				
			}

		}

		return true;

	}

	/**
	 * Checks if a cultural asset violates the max height of the hierarchy of
	 * cultural assets.
	 * 
	 * @param entity - the cultural asset.
	 * @return If there is an error, true if yes and false if no.
	 */
	private boolean testHeightError(CulturalAssetEntity entity) {

		if (entity.getLevel() >= 4) {

			return true;

		} else {

			List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();

			if (!children.isEmpty()) {
				
				children.stream().forEach((CulturalAssetEntity nextEntity) -> {
					this.testHeightError(nextEntity);
				});

			}

		}

		return false;

	}

	/**
	 * Updates the level of the cultural asset. This also updates all children below
	 * the cultural asset in the hierarchy of cultural assets.
	 * 
	 * @param entity - the cultural asset.
	 * @param level  - the new level of the cultural asset.
	 */
	private void updateLevels(CulturalAssetEntity entity, int level) {

		entity.setLevel(level);
		this.repository.save(entity);
		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();

		if (!children.isEmpty()) {
			
			children.stream().forEach((CulturalAssetEntity nextEntity) -> {
				this.updateLevels(nextEntity, level + 1);
			});

		}

	}

	/**
	 * Updates the endangered state of the cultural asset. This also updates the
	 * endangered state of all children below the cultural asset in the hierarchy of
	 * cultural assets. This also updates the endangered state of all connected
	 * tasks.
	 * 
	 * @param entity - the cultural asset.
	 * @param state  - the new endangered state of the cultural asset.
	 */
	public void updateIsEndangered(CulturalAssetEntity entity, int state) {
		
		// marks this cultural asset as endangered
		entity.setIsEndangered(state);
		this.repository.save(entity);
		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();
		List<TaskEntity> tasks = entity.getTasks();
		
		// marks all related tasks as endangered
		if (!tasks.isEmpty()) {
			
			tasks.stream().forEach((TaskEntity task) -> {
				task.setIsEndangered(state);
				taskRepository.save(task);
			});

		}
		
		// calls this method recursively for all children
		if (!children.isEmpty()) {
			
			children.stream().forEach((CulturalAssetEntity nextEntity) -> {
				this.updateIsEndangered(nextEntity, state);
			});

		}

	}

}
