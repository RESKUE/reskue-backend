package reskue.entities;

import java.util.List;

import kueres.entities.BaseService;

public abstract class ReskueService extends BaseService<ReskueEntity, ReskueRepository> {

	public List<ReskueEntity> findByName(String name) {
		
		return repository.findByName(name);
		
	}
	
}
