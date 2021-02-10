package reskue.notification;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
	//needs to block the update request
	
	
	//get all notifications from a specific user
	
	@GetMapping("/userNotifications/{" + UserEntity.ID + "}")
	@RolesAllowed({ "administrator", "helper" })
	public List<NotificationEntity> getAllUserNotifications(
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
		
		Sort sorting = Sort.unsorted();
		if (sort.isPresent()) {
			sorting = SortBuilder.buildSort(sort.get());
		}
		
		Pageable pagination = Pageable.unpaged();
		if (page.isPresent() && size.isPresent()) {
			pagination = PageRequest.of(page.get(), size.get());
		}

		//doesnt use filter and pagination yet
		
		return userService.findById(id).getNotificationReceiver();
		
	}

}
