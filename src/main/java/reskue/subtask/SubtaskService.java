package reskue.subtask;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import kueres.base.BaseService;

@Service
public class SubtaskService extends BaseService<SubtaskEntity, SubtaskRepository>{

	@Override
	@PostConstruct
	public void init() {
		
	}
	
	public SubtaskEntity changeState(long id, int state) {
		SubtaskEntity entity = this.findById(id);
		
		entity.setState(state);
		
		final SubtaskEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}
	
}
