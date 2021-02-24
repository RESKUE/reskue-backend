package reskue.culturalasset;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.location.LocationService;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;
import reskue.ReskueService;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.task.TaskRepository;

@Service
public class CulturalAssetService extends ReskueService<CulturalAssetEntity, CulturalAssetRepository> {

	@Autowired
	protected LocationService locationService;

	@Autowired
	protected TaskRepository taskRepository;

	@PersistenceContext
	private EntityManager em;

	@Override
	@PostConstruct
	public void init() {
		this.identifier = CulturalAssetController.ROUTE;
		this.routingKey = CulturalAssetController.ROUTE;
	}

	@Override
	public CulturalAssetEntity create(CulturalAssetEntity entity) {

		Utility.LOG.trace("CulturalAssetService.create called.");

		if (entity.getLongitude() != null && entity.getLatitude() != null) {
			if (entity.getAddress() == null) {
				entity.setAddress(locationService
						.coordinatesToAddress(new double[] { entity.getLongitude(), entity.getLatitude() }));
			}
		} else if (entity.getAddress() != null) {
			if (entity.getLongitude() == null && entity.getLatitude() == null) {
				double[] updatedCoordinates = locationService.addressToCoordinates(entity.getAddress());
				entity.setLongitude(updatedCoordinates[0]);
				entity.setLatitude(updatedCoordinates[1]);
			}
		}

		if (entity.getLongitude() != null && entity.getLatitude() != null) {
			entity.setLocationId(locationService.addPOI(entity.getName(),
					new double[] { entity.getLongitude(), entity.getLatitude() }));
		}

		CulturalAssetEntity savedEntity = repository.save(entity);

		EventConsumer.sendEvent("CulturalAssetService.create", EventType.CREATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(savedEntity));

		return savedEntity;

	}

	@Override
	public CulturalAssetEntity update(long id, CulturalAssetEntity details) throws ResourceNotFoundException {

		Utility.LOG.trace("CulturalAssetService.update called.");

		CulturalAssetEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		entity.applyPatch(details);

		if (details.getAddress() != null || details.getLongitude() != null || details.getLatitude() != null) {

			if (entity.getLongitude() != null && entity.getLatitude() != null) {
				if (entity.getAddress() == null) {
					entity.setAddress(locationService
							.coordinatesToAddress(new double[] { entity.getLongitude(), entity.getLatitude() }));
				}
			} else if (entity.getAddress() != null) {
				if (entity.getLongitude() == null && entity.getLatitude() == null) {
					double[] updatedCoordinates = locationService.addressToCoordinates(entity.getAddress());
					entity.setLongitude(updatedCoordinates[0]);
					entity.setLatitude(updatedCoordinates[1]);
				}
			}

			if (entity.getLongitude() != null && entity.getLatitude() != null) {
				if (entity.getLocationId() != null) {
					locationService.removePOI(entity.getLocationId());
				}
				entity.setLocationId(locationService.addPOI(entity.getName(),
						new double[] { entity.getLongitude(), entity.getLatitude() }));
			}

		}

		final CulturalAssetEntity savedEntity = repository.save(entity);

		EventConsumer.sendEvent("CulturalAssetService.update", EventType.UPDATE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(savedEntity));

		return savedEntity;

	}

	@Override
	public CulturalAssetEntity delete(long id) throws ResourceNotFoundException {

		Utility.LOG.trace("CulturalAssetService.delete called.");

		CulturalAssetEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		repository.delete(entity);

		if (entity.getLocationId() != null) {
			locationService.removePOI(entity.getLocationId());
		}

		EventConsumer.sendEvent("CulturalAssetService.delete", EventType.DELETE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(entity));

		return entity;

	}

	@SuppressWarnings("unchecked")
	public Page<CulturalAssetEntity> findInRadius(double radius, double longitude, double latitude,
			EntitySpecification<CulturalAssetEntity> specification, Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.findInRadius called.");

		List<String> entityIds = locationService.findInRadius(radius, new double[] { longitude, latitude });
		List<CulturalAssetEntity> entities = entityIds.stream().map(this.repository::findByLocationId)
				.collect(Collectors.toList());

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<CulturalAssetEntity> criteriaQuery = criteriaBuilder.createQuery(CulturalAssetEntity.class);
			Root<CulturalAssetEntity> root = criteriaQuery.from(CulturalAssetEntity.class);

			entities = entities.stream().filter((Predicate<? super CulturalAssetEntity>) specification.toPredicate(root,
					criteriaQuery, criteriaBuilder)).collect(Collectors.toList());
		}

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(entities, pageable, entities.size());

		EventConsumer.sendEvent("CulturalAssetService.findInRadius", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	@SuppressWarnings("unchecked")
	public Page<TaskEntity> getAllTasks(long id, EntitySpecification<TaskEntity> specification, Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.getAllTasks called.");

		CulturalAssetEntity entity = this.findById(id);
		List<TaskEntity> tasks = entity.getTasks();

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);
			Root<TaskEntity> root = criteriaQuery.from(TaskEntity.class);

			tasks = tasks.stream().filter(
					(Predicate<? super TaskEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<TaskEntity> page = new PageImpl<TaskEntity>(tasks, pageable, tasks.size());

		EventConsumer.sendEvent("CulturalAssetService.getAllTasks", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	@SuppressWarnings("unchecked")
	public Page<CulturalAssetEntity> getAllChildren(long id, EntitySpecification<CulturalAssetEntity> specification,
			Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.getAllChildren called.");

		CulturalAssetEntity entity = this.findById(id);
		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<CulturalAssetEntity> criteriaQuery = criteriaBuilder.createQuery(CulturalAssetEntity.class);
			Root<CulturalAssetEntity> root = criteriaQuery.from(CulturalAssetEntity.class);

			children = children.stream().filter((Predicate<? super CulturalAssetEntity>) specification.toPredicate(root,
					criteriaQuery, criteriaBuilder)).collect(Collectors.toList());
		}

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(children, pageable, children.size());

		EventConsumer.sendEvent("CulturalAssetService.getAllChildren", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	@SuppressWarnings("unchecked")
	public Page<NotificationEntity> getAllNotifications(long id, EntitySpecification<NotificationEntity> specification,
			Pageable pageable) {

		Utility.LOG.trace("CulturalAssetService.getAllNotifications called.");

		CulturalAssetEntity entity = this.findById(id);
		List<NotificationEntity> notifications = entity.getNotifications();

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<NotificationEntity> criteriaQuery = criteriaBuilder.createQuery(NotificationEntity.class);
			Root<NotificationEntity> root = criteriaQuery.from(NotificationEntity.class);

			notifications = notifications.stream().filter((Predicate<? super NotificationEntity>) specification
					.toPredicate(root, criteriaQuery, criteriaBuilder)).collect(Collectors.toList());
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(notifications, pageable, notifications.size());

		EventConsumer.sendEvent("CulturalAssetService.getAllNotifications", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(page));

		return page;

	}

	public double getDistance(long id, double longitude, double latitude) {

		Utility.LOG.trace("CulturalAssetService.getDistance called.");

		CulturalAssetEntity entity = this.findById(id);

		double[] entityLocation = new double[] { entity.getLongitude(), entity.getLatitude() };

		double distance = locationService.calculateDistance(entityLocation, new double[] { longitude, latitude });

		EventConsumer.sendEvent("CulturalAssetService.getDistance", EventType.READ.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(distance));

		return distance;

	}

	public CulturalAssetEntity addCulturalAssetChild(long id, long childId) {

		Utility.LOG.trace("CulturalAssetService.addCulturalAssetChild called.");

		CulturalAssetEntity parent = this.findById(id);
		CulturalAssetEntity child = this.findById(childId);
		CulturalAssetEntity updatedEntity = parent;

		this.addConnection(child, parent);
		
		if (this.testLoopError(child) || this.testHeightError(child)) {

			// throw error

		} else {
			
			updatedEntity = repository.save(parent);
			repository.save(child);
			
		}

		EventConsumer.sendEvent("CulturalAssetService.addCulturalAssetChild", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	public CulturalAssetEntity removeCulturalAssetChild(long id, long childId) {

		Utility.LOG.trace("CulturalAssetService.removeCulturalAssetChild called.");

		CulturalAssetEntity parent = this.findById(id);
		CulturalAssetEntity child = this.findById(childId);

		this.removeConnection(child, parent);

		final CulturalAssetEntity updatedEntity = repository.save(parent);

		repository.save(child);

		EventConsumer.sendEvent("CulturalAssetService.removeCulturalAssetChild", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	public CulturalAssetEntity setCulturalAssetParent(long id, long parentId) {

		Utility.LOG.trace("CulturalAssetService.setCulturalAssetParent called.");

		CulturalAssetEntity child = this.findById(id);
		CulturalAssetEntity parent = this.findById(parentId);
		CulturalAssetEntity updatedEntity = child;

		this.addConnection(child, parent);

		if (this.testLoopError(child) || this.testHeightError(child)) {

			// throw error

		} else {
			
			updatedEntity = repository.save(child);
			repository.save(parent);
			
		}

		EventConsumer.sendEvent("CulturalAssetService.setCulturalAssetParent", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

	public CulturalAssetEntity removeCulturalAssetParent(long id) {

		Utility.LOG.trace("CulturalAssetService.removeCulturalAssetParent called.");

		CulturalAssetEntity child = this.findById(id);
		CulturalAssetEntity parent = child.getCulturalAssetParent();

		this.removeConnection(child, parent);

		final CulturalAssetEntity updatedEntity = repository.save(child);

		repository.save(parent);

		EventConsumer.sendEvent("CulturalAssetService.removeCulturalAssetParent", EventType.UPDATE.type,
				this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));

		return updatedEntity;

	}

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

	private void updateLevels(CulturalAssetEntity entity, int level) {

		entity.setLevel(level);
		repository.save(entity);
		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();

		if (!children.isEmpty()) {
			children.stream().forEach((CulturalAssetEntity nextEntity) -> {
				this.updateLevels(nextEntity, level + 1);
			});
			
		}

	}

	private void updateIsEndangered(CulturalAssetEntity entity, int state) {

		entity.setIsEndangered(state);
		repository.save(entity);
		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();
		List<TaskEntity> tasks = entity.getTasks();

		if (!tasks.isEmpty()) {
			tasks.stream().forEach((TaskEntity task) -> {
				task.setIsEndangered(state);
				taskRepository.save(task);
			});

		}

		if (!children.isEmpty()) {
			children.stream().forEach((CulturalAssetEntity nextEntity) -> {
				this.updateIsEndangered(nextEntity, state);
			});

		}

	}

}
