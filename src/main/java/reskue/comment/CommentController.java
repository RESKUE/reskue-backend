package reskue.comment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + CommentController.ROUTE)
public class CommentController extends BaseController<CommentEntity, CommentRepository, CommentService>{
	
	public static final String ROUTE = "/comment";

}
