package kueres.entities.event;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import kueres.entities.BaseRepository;

public interface EventRepository extends BaseRepository<EventEntity>, JpaSpecificationExecutor<EventEntity> {

	
	
}
