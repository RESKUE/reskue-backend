package reskue.culturalasset;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
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
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<CulturalAssetEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<CulturalAssetEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();		// default sort
		int pageNumber = 0;					// default page number, starts at 0
		int pageSize = 25;					// default page size, 25
		
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}
		if (page.isPresent()) {
			pageNumber = page.get();
		}
		if (size.isPresent()) {
			pageSize = size.get();
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);
		
		return service.findInRadius(radius, longitude, latitude, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/tasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getAllTasks(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<TaskEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<TaskEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();		// default sort
		int pageNumber = 0;					// default page number, starts at 0
		int pageSize = 25;					// default page size, 25
		
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}
		if (page.isPresent()) {
			pageNumber = page.get();
		}
		if (size.isPresent()) {
			pageSize = size.get();
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);
		
		return service.getAllTasks(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/children")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CulturalAssetEntity> getAllChildren(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<CulturalAssetEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<CulturalAssetEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();		// default sort
		int pageNumber = 0;					// default page number, starts at 0
		int pageSize = 25;					// default page size, 25
		
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}
		if (page.isPresent()) {
			pageNumber = page.get();
		}
		if (size.isPresent()) {
			pageSize = size.get();
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);

		return service.getAllChildren(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/notifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getAllNotifications(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<NotificationEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<NotificationEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();		// default sort
		int pageNumber = 0;					// default page number, starts at 0
		int pageSize = 25;					// default page size, 25
		
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}
		if (page.isPresent()) {
			pageNumber = page.get();
		}
		if (size.isPresent()) {
			pageSize = size.get();
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);

		return service.getAllNotifications(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/distance")
	@RolesAllowed({ "administrator", "helper" })
	public double getDistance(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@RequestParam double longitude,
			@RequestParam double latitude) {

		return service.getDistance(id, longitude, latitude);
		
	}	
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/addChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> addCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) long childId) {

		CulturalAssetEntity updatedEntity = service.addCulturalAssetChild(id, childId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) long childId) {

		CulturalAssetEntity updatedEntity = service.removeCulturalAssetChild(id, childId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/setParent/{" + CulturalAssetEntity.CULTURAL_ASSET_PARENT + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> setCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_PARENT) long parentId) {

		CulturalAssetEntity updatedEntity = service.setCulturalAssetParent(id, parentId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeParent")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) long id) {

		CulturalAssetEntity updatedEntity = service.removeCulturalAssetParent(id);
		return ResponseEntity.ok().body(updatedEntity);
		
	}	

}
