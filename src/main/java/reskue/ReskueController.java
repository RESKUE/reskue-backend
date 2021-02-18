package reskue;

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
import org.springframework.web.bind.annotation.RequestParam;

import kueres.base.BaseController;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
import reskue.comment.CommentEntity;

public abstract class ReskueController<E extends ReskueEntity<E>, R extends ReskueRepository<E>, S extends ReskueService<E, R>>
		extends BaseController<E, R, S> {

	@GetMapping("/{" + ReskueEntity.ID + "}/comments")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CommentEntity> getAllComments(
			@PathVariable(value = ReskueEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<CommentEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<CommentEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();		// default sort
		int pageNumber = 0;					// default page number, starts at 0
		int pageSize = Integer.MAX_VALUE;	// default page size, might change to 20
		
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

		return service.getAllComments(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + ReskueEntity.ID + "}/media")
	@RolesAllowed({ "administrator", "helper" })
	public Page<MediaEntity> getAllMedia(
			@PathVariable(value = ReskueEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<MediaEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<MediaEntity>();
			for (String searchFilter : filters) {
				specification.add(new SearchCriteria(searchFilter));
			}
		}

		Sort sorting = Sort.unsorted();		// default sort
		int pageNumber = 0;					// default page number, starts at 0
		int pageSize = Integer.MAX_VALUE;	// default page size, might change to 20
		
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

		return service.getAllMedia(id, specification, pageable);
		
	}
	
	
	@PutMapping("/{" + ReskueEntity.ID + "}/addTag/{" + ReskueEntity.TAGS + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<E> addTag(
			@PathVariable(value = ReskueEntity.ID) long id,
			@PathVariable(value = ReskueEntity.TAGS) String tag) {

		E updatedEntity = service.addTag(id, tag);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

	@PutMapping("/{" + ReskueEntity.ID + "}/removeTag/{" + ReskueEntity.TAGS + "}")
	@RolesAllowed("administrator")
	public ResponseEntity<E> removeTag(
			@PathVariable(value = ReskueEntity.ID) long id,
			@PathVariable(value = ReskueEntity.TAGS) String tag) {

		E updatedEntity = service.removeTag(id, tag);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
}
