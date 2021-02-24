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

@RestController
@RequestMapping(BaseController.API_ENDPOINT + UserGroupController.ROUTE)
public class UserGroupController extends BaseController<UserGroupEntity, UserGroupRepository, UserGroupService>{
	
	public static final String ROUTE = "/userGroup";
	
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
	
	@PutMapping("/{" + UserGroupEntity.ID + "}/addUser/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator" })
	public ResponseEntity<UserGroupEntity> addUser(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@PathVariable(value = UserEntity.ID) long userId) {
		
		Utility.LOG.trace("UserGroupController.addUser called.");

		UserGroupEntity updatedEntity = service.addUser(id, userId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
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
