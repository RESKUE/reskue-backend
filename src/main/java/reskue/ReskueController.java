package reskue;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import kueres.base.BaseController;
import kueres.eventbus.EventConsumer;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;

/**
 * 
 * The ReskueController provides functionality for the fields of a ReskueEntity.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - finding all comments of an entity
 *  - finding all media of an entity
 *  
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

public abstract class ReskueController<E extends ReskueEntity<E>, R extends ReskueRepository<E>, S extends ReskueService<E, R>>
		extends BaseController<E, R, S> {
	
	/**
	 * Find all media of the controllers ReskueEntity-type.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the entity's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + ReskueEntity.ID + "}/media")
	@RolesAllowed({ "administrator", "helper" })
	public Page<MediaEntity> getAllMedia(
			@PathVariable(value = ReskueEntity.ID) Long id,
			@RequestParam Optional<String[]> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		Utility.LOG.trace("ReskueController.getAllComments called.");
		
		Utility.LOG.info("id: {}", id);
		
		EntitySpecification<MediaEntity> specification = null;
		if (filter.isPresent()) {
			
			Utility.LOG.info("filter: {}", EventConsumer.writeObjectAsJSON(filter.get()));
			specification = new EntitySpecification<MediaEntity>(filter.get());
			
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);

		return service.getAllMedia(id, specification, pageable);
		
	}
	
}
