package reskue.usergroup;

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
import reskue.user.UserEntity;

/**
 * 
 * The UserGroupController provides API functions for UserGroupEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - finding all users of a user group
 *  - adding a user to a user group
 *  - removing a user from a user group
 *
 * @author Jan Straﬂburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + UserGroupController.ROUTE)
public class UserGroupController extends BaseController<UserGroupEntity, UserGroupRepository, UserGroupService>{
	
	/**
	 * The API route for UserGroupEntites.
	 */
	public static final String ROUTE = "/userGroup";
	
	/**
	 * Find all users that are part of the user group.
	 * 
	 * The result can filtered, sorted and paged.
	 * <p>
	 * See kueres.query.SearchCriteria for filter syntax.
	 * <p>
	 * See kueres.query.SortBuilder for sort syntax.
	 * 
	 * @param id - the user groups's identifier.
	 * @param filter - the filter options.
	 * @param sort - the sort options.
	 * @param page - the number of the page used for pagination.
	 * @param size - the size of the page used for pagination.
	 * @return The result as a page.
	 */
	@GetMapping("/{" + UserGroupEntity.ID + "}/users")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserEntity> getAllUsers(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@RequestParam Optional<String[]> filter,
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size
			) {
		
		Utility.LOG.trace("UserGroupController.getAllUsers called.");
		
		EntitySpecification<UserEntity> specification = null;
		if (filter.isPresent()) {
			specification = new EntitySpecification<UserEntity>(filter.get());
		}
		
		Pageable pageable = SortBuilder.buildPageable(sort, page, size);
		
		return service.getAllUsers(id, specification, pageable);
			
	}
	
	/**
	 * Adds a user to the user group.
	 * 
	 * @param id - the user groups's identifier.
	 * @param userId - the user's identifier.
	 * @return The user group after the user was added.
	 */
	@PutMapping("/{" + UserGroupEntity.ID + "}/addUser/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator" })
	public ResponseEntity<UserGroupEntity> addUser(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@PathVariable(value = UserEntity.ID) long userId) {
		
		Utility.LOG.trace("UserGroupController.addUser called.");

		UserGroupEntity updatedEntity = service.addUser(id, userId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	/**
	 * Removes a user from the user group.
	 * 
	 * @param id - the user groups's identifier.
	 * @param userId - the user's identifier.
	 * @return The user group after the user was removed.
	 */
	@PutMapping("/{" + UserGroupEntity.ID + "}/removeUser/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator" })
	public ResponseEntity<UserGroupEntity> removeUser(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@PathVariable(value = UserEntity.ID) long userId) {
		
		Utility.LOG.trace("UserGroupController.removeUser called.");

		UserGroupEntity updatedEntity = service.removeUser(id, userId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
