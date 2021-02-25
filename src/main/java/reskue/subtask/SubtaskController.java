package reskue.subtask;

import javax.annotation.security.RolesAllowed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kueres.base.BaseController;
import kueres.utility.Utility;

/**
 * 
 * The SubtaskController provides API functions for SubtaskEntities.
 * These functions are:
 *  - all functions of the BaseController in kueres.base
 *  - changing the state of a subtask
 *
 * @author Jan Straﬂburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@RestController
@RequestMapping(BaseController.API_ENDPOINT + SubtaskController.ROUTE)
public class SubtaskController extends BaseController<SubtaskEntity, SubtaskRepository, SubtaskService>{
	
	/**
	 * The API route for SubtaskEntites.
	 */
	public static final String ROUTE = "/subtask";
	
	/**
	 * Change the state of the subtask.
	 * 
	 * @param id - the subtask's identifier.
	 * @param state - the new state.
	 * @return The subtask after state was changed.
	 */
	@PutMapping("/{" + SubtaskEntity.ID + "}/changeState/{" + SubtaskEntity.STATE + "}")
	@RolesAllowed({ "administrator", "helper" })
	public ResponseEntity<SubtaskEntity> changeState(
			@PathVariable(value = SubtaskEntity.ID) long id,
			@PathVariable(value = SubtaskEntity.STATE) int state) {
		
		Utility.LOG.trace("SubtaskController.changeState called.");
		
		SubtaskEntity updatedEntity = service.changeState(id, state);
		return ResponseEntity.ok().body(updatedEntity);
		
	}

}
