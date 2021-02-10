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

import kueres.location.LocationService;
import kueres.query.EntitySpecification;
import reskue.ReskueService;
import reskue.task.TaskEntity;

public class CulturalAssetService extends ReskueService<CulturalAssetEntity, CulturalAssetRepository>{
	
	@Autowired
	protected LocationService locationService;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@PostConstruct
	public void init() {
		this.identifier = CulturalAssetController.ROUTE;
		this.routingKey = CulturalAssetController.ROUTE;
		this.startReceivingEvents();
	}
	
	@SuppressWarnings("unchecked")
	public Page<CulturalAssetEntity> findInRadius(double radius, double longitude, double latitiude,
			EntitySpecification<CulturalAssetEntity> specification, Pageable pageable) {
	
		List<Long> entityIds = locationService.findInRadius(radius, new double[] {longitude, latitiude});
		
		List<CulturalAssetEntity> entities = entityIds.stream().map(this::findById).collect(Collectors.toList());
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<CulturalAssetEntity> criteriaQuery = criteriaBuilder.createQuery(CulturalAssetEntity.class);
		Root<CulturalAssetEntity> root = criteriaQuery.from(CulturalAssetEntity.class);

		entities = entities.stream().filter(
				(Predicate<? super CulturalAssetEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(entities, pageable, entities.size());

		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<TaskEntity> getAllTasks(long id, EntitySpecification<TaskEntity> specification, Pageable pageable) {

		CulturalAssetEntity entity = this.findById(id);

		List<TaskEntity> tasks = entity.getTasks();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);
		Root<TaskEntity> root = criteriaQuery.from(TaskEntity.class);

		tasks = tasks.stream().filter(
				(Predicate<? super TaskEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<TaskEntity> page = new PageImpl<TaskEntity>(tasks, pageable, tasks.size());

		return page;
		
	}

	@SuppressWarnings("unchecked")
	public Page<CulturalAssetEntity> getAllChildren(long id, EntitySpecification<CulturalAssetEntity> specification,
			Pageable pageable) {

		CulturalAssetEntity entity = this.findById(id);

		List<CulturalAssetEntity> children = entity.getCulturalAssetChildren();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<CulturalAssetEntity> criteriaQuery = criteriaBuilder.createQuery(CulturalAssetEntity.class);
		Root<CulturalAssetEntity> root = criteriaQuery.from(CulturalAssetEntity.class);

		children = children.stream().filter(
				(Predicate<? super CulturalAssetEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<CulturalAssetEntity> page = new PageImpl<CulturalAssetEntity>(children, pageable, children.size());

		return page;
		
	}
	
	public double getDistance(long id, double longitude, double latitude) {
		
		CulturalAssetEntity entity = this.findById(id);
		
		double[] entityLocation = new double[] {entity.getLongitude(), entity.getLatitude()};
		
		return locationService.calculateDistance(entityLocation, new double[] {longitude, latitude});
		
	}

	public CulturalAssetEntity addCulturalAssetChild(long id, long childId) {
		
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
		
		CulturalAssetEntity entity = this.findById(id);
		CulturalAssetEntity parent = this.findById(parentId);
		
		entity.setCulturalAssetParent(parent);
		
		final CulturalAssetEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

	public CulturalAssetEntity removeCulturalAssetParent(long id) {
		
		CulturalAssetEntity entity = this.findById(id);
		
		entity.setCulturalAssetParent(null);
		
		final CulturalAssetEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

}
