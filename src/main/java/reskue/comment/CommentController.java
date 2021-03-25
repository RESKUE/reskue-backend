package reskue.comment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.query.SortBuilder;
import kueres.utility.Utility;
import reskue.ReskueEntity;
import reskue.user.UserEntity;
import reskue.user.UserService;

/**
 * 
 * The CommentController provides API functions for CommentEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - finding all media of a comment
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + CommentController.ROUTE)
public class CommentController extends BaseController<CommentEntity, CommentRepository, CommentService>{
		
	/**
	 * The API route for CommentEntities.
	 */
	public static final String ROUTE = "/comment";
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/autoAuthor")
	@RolesAllowed("administrator")
	public ResponseEntity<CommentEntity> createAutoAuthor(
			HttpServletRequest request, 
			HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		Utility.LOG.trace("NotificationController.create called.");
		
		KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
		AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
		
		String subject = token.getSubject();
		UserEntity author = userService.me(subject);
		
		String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		Class<CommentEntity> entityClass = this.service.getEntityClass();
		CommentEntity entity = entityClass.getDeclaredConstructor().newInstance();
		entity = BaseEntity.createEntityFromJSON(body, entity.getUpdateableFields(), entityClass);
		entity.setAuthor(author);
		
		CommentEntity created = this.service.create(entity);
		return ResponseEntity.ok().body(created);
		
	}
	
	/**
	 * Find all media of the comment.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the comment's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + CommentEntity.ID + "}/media")
	@RolesAllowed({ "administrator", "helper" })
	public Page<MediaEntity> getAllMedia(
			@PathVariable(value = ReskueEntity.ID) Long id,
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
