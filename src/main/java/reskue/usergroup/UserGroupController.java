package reskue.usergroup;

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
import reskue.user.UserEntity;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + UserGroupController.ROUTE)
public class UserGroupController extends BaseController<UserGroupEntity, UserGroupRepository, UserGroupService>{
	
	public static final String ROUTE = "/userGroup";
	
	@GetMapping("/{" + UserGroupEntity.ID + "}/users")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserEntity> getAllUsers(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<UserEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<UserEntity>();
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
		
		return service.getAllUsers(id, specification, pageable);
			
	}
	
	@PutMapping("/{" + UserGroupEntity.ID + "}/addUser/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<UserGroupEntity> addUser(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@PathVariable(value = UserEntity.ID) long userId) {

		UserGroupEntity updatedEntity = service.addUser(id, userId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}
	
	@PutMapping("/{" + UserGroupEntity.ID + "}/removeUser/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<UserGroupEntity> removeUser(
			@PathVariable(value = UserGroupEntity.ID) long id,
			@PathVariable(value = UserEntity.ID) long userId) {

		UserGroupEntity updatedEntity = service.removeUser(id, userId);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
