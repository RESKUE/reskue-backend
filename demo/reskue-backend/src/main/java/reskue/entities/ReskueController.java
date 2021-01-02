package reskue.entities;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kueres.entities.BaseController;

public abstract class ReskueController extends BaseController<ReskueEntity, ReskueRepository, ReskueService>{
	
	@GetMapping(ReskueEntity.NAME_MAPPING)
	@RolesAllowed({"administrator", "helper"})
	public List<ReskueEntity> findByName(
			@PathVariable(value = ReskueEntity.NAME) String name
			) {
		
		return service.findByName(name);
		
	}
	
}
