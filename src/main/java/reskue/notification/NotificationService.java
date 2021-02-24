package reskue.notification;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.base.BaseService;

@Service
public class NotificationService extends BaseService<NotificationEntity, NotificationRepository>{

	@Override
	@PostConstruct
	public void init() {
		
	}

}
