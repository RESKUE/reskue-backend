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
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;

/**
 * 
 * The TaskController provides API functions for TaskEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - all functions of the ReskueController in reskue
 *  - finding all cultural assets in a radius around a given point
 *  - finding all tasks of a cultural asset
 *  - finding all children of a cultural asset
 *  - finding all notifications of a cultural asset
 *  - calculating the distance from a point to a cultural asset
 *  - adding a child to a cultural asset
 *  - removing a child from a cultural asset
 *  - setting the parent of a cultural asset
 *  - removing the parent from a cultural asset
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + CulturalAssetController.ROUTE)
public class CulturalAssetController extends ReskueController<CulturalAssetEntity, CulturalAssetRepository, CulturalAssetService>{
	
	/**
	 * The API route for CulturalAssetEntities.
	 */
	public static final String ROUTE = "/culturalAsset";
	
	/**
	 * Find all cultural assets in a radius around a given middle point.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param radius - the radius of the search.
	 * @param longitude - the longitude of the middle of the search.
	 * @param latitude - the latitude of the middle of the search.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
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
	
	/**
	 * Calculate the distance between a given point and the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param longitude - the longitude of the given point
	 * @param latitude - the latitude of the given point
	 * @return The result in meters as a double
	 */
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/distance")
	@RolesAllowed({ "administrator", "helper" })
	public double getDistance(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
			@RequestParam double longitude,
			@RequestParam double latitude) {
		
		Utility.LOG.trace("CulturalAssetController.getDistance called.");
		
		return service.getDistance(id, longitude, latitude);
		
	}	
	
	/**
	 * Find all tasks of the cultural asset.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/tasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getAllTasks(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
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
	
	/**
	 * Find all comments of the cultural asset.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/comments")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CommentEntity> getAllComments(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
			@RequestParam Optional<String[]> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		Utility.LOG.trace("ReskueController.getAllComments called.");
		
		EntitySpecification<CommentEntity> specification = null;
		if (filter.isPresent()) {
			
			specification = new EntitySpecification<CommentEntity>(filter.get());
			
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);

		return service.getAllComments(id, specification, pageable);
		
	}
	
	/**
	 * Find all children of the cultural asset.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/children")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CulturalAssetEntity> getAllChildren(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
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
	
	/**
	 * Find all notifications of the cultural asset.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + CulturalAssetEntity.ID + "}/notifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getAllNotifications(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
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
	
	/**
	 * Adds a child to the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param childId - the childs's identifier.
	 * @return The cultural asset after the child was added.
	 */
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/addChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> addCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) Long childId) {
		
		Utility.LOG.trace("CulturalAssetController.addCulturalAssetChild called.");
		
		CulturalAssetEntity updatedEntity = service.addCulturalAssetChild(id, childId);
		
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	/**
	 * Removes a child from the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param childId - the childs's identifier.
	 * @return The cultural asset after the child was removed.
	 */
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeChild/{" + CulturalAssetEntity.CULTURAL_ASSET_CHILDREN + "}")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetChild(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_CHILDREN) Long childId) {
		
		Utility.LOG.trace("CulturalAssetController.removeCulturalAssetChild called.");
		
		CulturalAssetEntity updatedEntity = service.removeCulturalAssetChild(id, childId);
		
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	/**
	 * Sets the parent of the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @param parentId - the childs's identifier.
	 * @return The cultural asset after the parent was set.
	 */
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/setParent/{" + CulturalAssetEntity.CULTURAL_ASSET_PARENT + "}")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> setCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) Long id,
			@PathVariable(value = CulturalAssetEntity.CULTURAL_ASSET_PARENT) Long parentId) {
		
		Utility.LOG.trace("CulturalAssetController.setCulturalAssetParent called.");
		
		CulturalAssetEntity updatedEntity = service.setCulturalAssetParent(id, parentId);
		
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	/**
	 * Removes the parent of the cultural asset.
	 * 
	 * @param id - the cultural asset's identifier.
	 * @return The cultural asset after the parent was removed.
	 */
	@PutMapping("/{" + CulturalAssetEntity.ID + "}/removeParent")
	@RolesAllowed( "administrator" )
	public ResponseEntity<CulturalAssetEntity> removeCulturalAssetParent(
			@PathVariable(value = CulturalAssetEntity.ID) Long id) {
		
		Utility.LOG.trace("CulturalAssetController.removeCulturalAssetParent called.");
		
		CulturalAssetEntity updatedEntity = service.removeCulturalAssetParent(id);
		
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
