package reskue.comment;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;
import reskue.ReskueEntity;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + CommentController.ROUTE)
public class CommentController extends BaseController<CommentEntity, CommentRepository, CommentService>{
	
	public static final String ROUTE = "/comment";
	
	@GetMapping("/{" + CommentEntity.ID + "}/media")
	@RolesAllowed({ "administrator", "helper" })
	public Page<MediaEntity> getAllMedia(
			@PathVariable(value = ReskueEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("CommentController.getAllMedia called.");
		
		EntitySpecification<MediaEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<MediaEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);

		return service.getAllMedia(id, specification, pageable);
		
	}

}
