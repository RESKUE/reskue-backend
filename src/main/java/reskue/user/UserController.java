package reskue.user;

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
import kueres.query.EntitySpecification;
import kueres.query.SearchCriteria;
import kueres.query.SortBuilder;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;
import reskue.usergroup.UserGroupEntity;


@RestController
@RequestMapping(BaseController.API_ENDPOINT + UserController.ROUTE)
public class UserController extends BaseController<UserEntity, UserRepository, UserService>{
	
	public static final String ROUTE = "/user";

	@GetMapping("/{" + UserEntity.ID + "}/contactTasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getTasksWhereUserIsContact(
			@PathVariable(value = UserEntity.ID) long id,
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
		
		return service.getTasksWhereUserIsContact(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/helperTasks")
	@RolesAllowed({ "administrator", "helper" })
	public Page<TaskEntity> getTasksWhereUserIsHelper(
			@PathVariable(value = UserEntity.ID) long id,
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
		
		return service.getTasksWhereUserIsHelper(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/userComments")
	@RolesAllowed({ "administrator", "helper" })
	public Page<CommentEntity> getCommentsByUser(
			@PathVariable(value = UserEntity.ID) long id,
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
		
		return service.getCommentsByUser(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/userGroups")
	@RolesAllowed({ "administrator", "helper" })
	public Page<UserGroupEntity> getUserGroupsForUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<UserGroupEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<UserGroupEntity>();
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
		
		return service.getUserGroupsForUser(id, specification, pageable);
			
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/sendNotifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getNotificationsSendByUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<NotificationEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<NotificationEntity>();
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
		
		return service.getNotificationsSendByUser(id, specification, pageable);
		
	}
	
	@GetMapping("/{" + UserEntity.ID + "}/receivedNotifications")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getNotificationsForUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size) {

		EntitySpecification<NotificationEntity> specification = null;
		if (filter.isPresent()) {
			String[] filters = filter.get().split(",");
			specification = new EntitySpecification<NotificationEntity>();
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
		
		return service.getNotificationsForUser(id, specification, pageable);
		
	}

}
