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

@Service
public class CulturalAssetService extends ReskueService<CulturalAssetEntity, CulturalAssetRepository>{
	
	@Autowired
	protected LocationService locationService;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@PostConstruct
	public void init() {

	}
	
	@Override
	public CulturalAssetEntity create(CulturalAssetEntity entity) {
		
		Utility.LOG.trace("CulturalAssetService.create called.");
		
		if (entity.getLongitude() != null && entity.getLatitude() != null) {		
			if (entity.getAddress() == null) {		
				entity.setAddress(locationService.coordinatesToAddress(new double[] {entity.getLongitude(), entity.getLatitude()}));
			}			
		} else if (entity.getAddress() != null) {		
			if (entity.getLongitude() == null && entity.getLatitude() == null) {				
				double[] updatedCoordinates = locationService.addressToCoordinates(entity.getAddress());
				entity.setLongitude(updatedCoordinates[0]);
				entity.setLatitude(updatedCoordinates[1]);				
			}			
		}
		
		if (entity.getLongitude() != null && entity.getLatitude() != null) {			
			entity.setLocationId(locationService.addPOI(entity.getName(), new double[] {entity.getLongitude(), entity.getLatitude()}));			
		}
		
		CulturalAssetEntity savedEntity = repository.save(entity);
		
		return savedEntity;
		
	}
	
	@Override
	public CulturalAssetEntity update(long id, CulturalAssetEntity details) throws ResourceNotFoundException {
		
		Utility.LOG.trace("CulturalAssetService.update called.");	
		
		CulturalAssetEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		entity.applyPatch(details);
		
		if (details.getAddress() != null || details.getLongitude() != null || details.getLatitude() != null) {
			
			if (entity.getLongitude() != null && entity.getLatitude() != null) {		
				if (entity.getAddress() == null) {		
					entity.setAddress(locationService.coordinatesToAddress(new double[] {entity.getLongitude(), entity.getLatitude()}));
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
				entity.setLocationId(locationService.addPOI(entity.getName(), new double[] {entity.getLongitude(), entity.getLatitude()}));			
			}
			
		}
		
		CulturalAssetEntity savedEntity = repository.save(entity);
		
		return savedEntity;
		
	}
	
	@Override
	public CulturalAssetEntity delete(long id) throws ResourceNotFoundException {
		
		Utility.LOG.trace("CulturalAssetService.delete called.");
		
		CulturalAssetEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		repository.delete(entity);
		
		if (entity.getLocationId() != null) {
			locationService.removePOI(entity.getLocationId());
		}
		
		return entity;
		
	}
	
	@SuppressWarnings("unchecked")
	public Page<CulturalAssetEntity> findInRadius(double radius, double longitude, double latitude,
			EntitySpecification<CulturalAssetEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("CulturalAssetService.findInRadius called.");
		
		List<String> entityIds = locationService.findInRadius(radius, new double[] {longitude, latitude});		
		List<CulturalAssetEntity> entities = entityIds.stream().map(this.repository::findByLocationId).collect(Collectors.toList());
		
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<CulturalAssetEntity> criteriaQuery = criteriaBuilder.createQuery(CulturalAssetEntity.class);
			Root<CulturalAssetEntity> root = criteriaQuery.from(CulturalAssetEntity.class);

			entities = entities.stream().filter(
					(Predicate<? super CulturalAssetEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(entities, pageable, entities.size());

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

			children = children.stream().filter(
					(Predicate<? super CulturalAssetEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(children, pageable, children.size());

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

			notifications = notifications.stream().filter(
					(Predicate<? super NotificationEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<NotificationEntity> page = new PageImpl<NotificationEntity>(notifications, pageable, notifications.size());

		return page;
		
	}
	
	public double getDistance(long id, double longitude, double latitude) {
		
		Utility.LOG.trace("CulturalAssetService.getDistance called.");
		
		CulturalAssetEntity entity = this.findById(id);
		
		double[] entityLocation = new double[] {entity.getLongitude(), entity.getLatitude()};
		
		return locationService.calculateDistance(entityLocation, new double[] {longitude, latitude});
		
	}

	public CulturalAssetEntity addCulturalAssetChild(long id, long childId) {
		
		Utility.LOG.trace("CulturalAssetService.addCulturalAssetChild called.");
		
		CulturalAssetEntity entity = this.findById(id);		
		CulturalAssetEntity child = this.findById(childId);		
		List<CulturalAssetEntity> newChildren = entity.getCulturalAssetChildren();
		
		//if the new child is already a child
		if(newChildren.contains(child)) {
			return entity;
		} else {
			newChildren.add(child);
			entity.setCulturalAssetChildren(newChildren);
		}
		
		final CulturalAssetEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

	public CulturalAssetEntity removeCulturalAssetChild(long id, long childId) {
		
		Utility.LOG.trace("CulturalAssetService.removeCulturalAssetChild called.");
		
		CulturalAssetEntity entity = this.findById(id);
		CulturalAssetEntity child = this.findById(childId);	
		List<CulturalAssetEntity> newChildren = entity.getCulturalAssetChildren();
		
		//if the child is actually a child
		if(newChildren.contains(child)) {
			newChildren.remove(child);
			entity.setCulturalAssetChildren(newChildren);
		} else {
			return entity;
		}
		
		final CulturalAssetEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

	public CulturalAssetEntity setCulturalAssetParent(long id, long parentId) {
		
		Utility.LOG.trace("CulturalAssetService.setCulturalAssetParent called.");
		
		CulturalAssetEntity entity = this.findById(id);
		CulturalAssetEntity parent = this.findById(parentId);
		
		entity.setCulturalAssetParent(parent);
		
		final CulturalAssetEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

	public CulturalAssetEntity removeCulturalAssetParent(long id) {
		
		Utility.LOG.trace("CulturalAssetService.removeCulturalAssetParent called.");
		
		CulturalAssetEntity entity = this.findById(id);	
		entity.setCulturalAssetParent(null);
		
		final CulturalAssetEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

}
