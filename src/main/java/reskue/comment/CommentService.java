package reskue.comment;

import javax.annotation.PostConstruct;

import kueres.base.BaseService;

public class CommentService extends BaseService<CommentEntity, CommentRepository>{

	@Override
	@PostConstruct
	public void init() {
		this.identifier = CommentController.ROUTE;
		this.routingKey = CommentController.ROUTE;
		this.startReceivingEvents();
	}

}
