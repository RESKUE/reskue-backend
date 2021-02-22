package reskue.comment;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
import reskue.ReskueEntity;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + CommentController.ROUTE)
public class CommentController extends BaseController<CommentEntity, CommentRepository, CommentService>{
	
	public static final String ROUTE = "/comment";
	
	@GetMapping("/{" + CommentEntity.ID + "}/media")
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

		return service.getAllMedia(id, specification, pageable);
		
	}

}
