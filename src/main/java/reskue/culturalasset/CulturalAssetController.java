package reskue.culturalasset;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;
import reskue.ReskueController;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + CulturalAssetController.ROUTE)
public class CulturalAssetController extends ReskueController<CulturalAssetEntity, CulturalAssetRepository, CulturalAssetService>{
	
	public static final String ROUTE = "/culturalAsset";
	
	@GetMapping("/radius")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CulturalAssetEntity> findInRadius(
			@RequestParam double radius,
			@RequestParam double longitude,
			@RequestParam double latitude,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("CulturalAssetController.findInRadius called.");
		
		EntitySpecification<CulturalAssetEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<CulturalAssetEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.findInRadius(radius, longitude, latitude, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/tasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getAllTasks(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("CulturalAssetController.getAllTasks called.");
		
		EntitySpecification<TaskEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<TaskEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getAllTasks(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/children")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CulturalAssetEntity> getAllChildren(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("CulturalAssetController.getAllChildren called.");
		
		EntitySpecification<CulturalAssetEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<CulturalAssetEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);

		return service.getAllChildren(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/notifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getAllNotifications(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("CulturalAssetController.getAllNotifications called.");
		
		EntitySpecification<NotificationEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<NotificationEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);

		return service.getAllNotifications(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/distance")
	@RolesAllowed({ "administrator", "helper" })
	public double getDistance(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam double longitude,
			@RequestParam double latitude) {
		
		Utility.LOG.trace("CulturalAssetController.getDistance called.");
		
		return service.getDistance(id, longitude, latitude);
		
	}	
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/addChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> addCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) long childId) {
		
		Utility.LOG.trace("CulturalAssetController.addCulturalAssetChild called.");
		
		CulturalAssetEntity updatedEntity = service.addCulturalAssetChild(id, childId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) long childId) {
		
		Utility.LOG.trace("CulturalAssetController.removeCulturalAssetChild called.");
		
		CulturalAssetEntity updatedEntity = service.removeCulturalAssetChild(id, childId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/setParent/{" + CulturalAssetEntity.CULTURAL_ASSET_PARENT + "}")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> setCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_PARENT) long parentId) {
		
		Utility.LOG.trace("CulturalAssetController.setCulturalAssetParent called.");
		
		CulturalAssetEntity updatedEntity = service.setCulturalAssetParent(id, parentId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeParent")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) long id) {
		
		Utility.LOG.trace("CulturalAssetController.removeCulturalAssetParent called.");
		
		CulturalAssetEntity updatedEntity = service.removeCulturalAssetParent(id);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@GetMapping("/test")
	@RolesAllowed( "administrator" )
	public void test() {
		
	}

}
