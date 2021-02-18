package reskue.notification;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
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
import reskue.user.UserEntity;
import reskue.user.UserService;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + NotificationController.ROUTE)
public class NotificationController
		extends BaseController<NotificationEntity, NotificationRepository, NotificationService> {

	public static final String ROUTE = "/notification";

	@Autowired
	protected UserService userService;	
	
	@GetMapping("/userNotifications/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator", "helper" })
	public Page<NotificationEntity> getNotificationsForUser(
			@PathVariable(value = UserEntity.ID) long id,
			@RequestParam Optional<String> filter, 
			@RequestParam Optional<String[]> sort,
			@RequestParam Optional<Integer> page, 
			@RequestParam Optional<Integer> size
			) {
		
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
		
		Pageable pagination = PageRequest.of(pageNumber, pageSize, sorting);
		
		return userService.getNotificationsForUser(id, specification, pagination);
		
	}

}
