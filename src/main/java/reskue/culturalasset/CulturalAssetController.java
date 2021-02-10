package reskue.culturalasset;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import kueres.location.LocationService;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
import reskue.ReskueController;
import reskue.task.TaskEntity;

public class CulturalAssetController extends ReskueController<CulturalAssetEntity, CulturalAssetRepository, CulturalAssetService>{
	
	public static final String ROUTE = "/culturalAsset";

	@Autowired
	protected LocationService locationService;
	
	//@GetMapping("/radius/{" + CulturalAssetEntity + "}")
	
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

		Sort sorting = Sort.unsorted();
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}

		Pageable pagination = Pageable.unpaged();
		if (page.isPresent() && size.isPresent()) {
			pagination = PageRequest.of(page.get(), size.get());
		}

		pagination = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sorting);

		return service.getAllTasks(id, specification, pagination);
		
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

		Sort sorting = Sort.unsorted();
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}

		Pageable pagination = Pageable.unpaged();
		if (page.isPresent() && size.isPresent()) {
			pagination = PageRequest.of(page.get(), size.get());
		}

		pagination = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sorting);

		return service.getAllChildren(id, specification, pagination);
		
	}
	
	//@GetMapping("/{" + CulturalAssetEntity.ID + "}/distance")
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/addChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> addCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) long childId, 
			@Valid @RequestBody CulturalAssetEntity details)
			throws ResourceNotFoundException {

		CulturalAssetEntity updatedEntity = service.addCulturalAssetChild(id, childId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) long childId, 
			@Valid @RequestBody CulturalAssetEntity details)
			throws ResourceNotFoundException {

		CulturalAssetEntity updatedEntity = service.removeCulturalAssetChild(id, childId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/setParent/{" + CulturalAssetEntity.CULTURAL_ASSET_PARENT + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> setCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_PARENT) long parentId, 
			@Valid @RequestBody CulturalAssetEntity details)
			throws ResourceNotFoundException {

		CulturalAssetEntity updatedEntity = service.setCulturalAssetParent(id, parentId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeParent")
	@RolesAllowed("administrator")
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) long id,
			@Valid @RequestBody CulturalAssetEntity details)
			throws ResourceNotFoundException {

		CulturalAssetEntity updatedEntity = service.removeCulturalAssetParent(id);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	

}
