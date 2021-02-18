package reskue.subtask;

import javax.annotation.security.RolesAllowed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;

@RestController
@RequestMapping(BaseController.API_ENDPOINT + SubtaskController.ROUTE)
public class SubtaskController extends BaseController<SubtaskEntity, SubtaskRepository, SubtaskService>{
	
	public static final String ROUTE = "/subtask";
	
	@PutMapping("/{" + SubtaskEntity.ID + "}/changeState/{" + SubtaskEntity.STATE + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<SubtaskEntity> changeState(
			@PathVariable(value = SubtaskEntity.ID) long id,
			@PathVariable(value = SubtaskEntity.STATE) int state) {

		SubtaskEntity updatedEntity = service.changeState(id, state);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
