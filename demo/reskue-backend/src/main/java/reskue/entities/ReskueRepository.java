package reskue.entities;

import java.util.List;

import kueres.entities.BaseRepository;

public interface ReskueRepository extends BaseRepository<ReskueEntity> {

	List<ReskueEntity> findByName(String name);
	
}
