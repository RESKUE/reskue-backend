package reskue.comment;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.base.BaseService;

@Service
public class CommentService extends BaseService<CommentEntity, CommentRepository>{

	@Override
	@PostConstruct
	public void init() {

	}

}
