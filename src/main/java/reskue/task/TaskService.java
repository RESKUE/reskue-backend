package reskue.task;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kueres.query.EntitySpecification;
import reskue.ReskueService;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;

@Service
public class TaskService extends ReskueService<TaskEntity, TaskRepository>{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@PostConstruct
	public void init() {
		
	}

	@SuppressWarnings("unchecked")
	public Page<SubtaskEntity> getAllSubtasks(long id, EntitySpecification<SubtaskEntity> specification,
			Pageable pageable) {	
		
		TaskEntity entity = this.findById(id);

		List<SubtaskEntity> subtasks = entity.getSubtasks();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<SubtaskEntity> criteriaQuery = criteriaBuilder.createQuery(SubtaskEntity.class);
		Root<SubtaskEntity> root = criteriaQuery.from(SubtaskEntity.class);

		subtasks = subtasks.stream().filter(
				(Predicate<? super SubtaskEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<SubtaskEntity> page = new PageImpl<SubtaskEntity>(subtasks, pageable, subtasks.size());

		return page;
		
	}

	@SuppressWarnings("unchecked")
	public Page<UserEntity> getAllHelpers(long id, EntitySpecification<UserEntity> specification, Pageable pageable) {
		
		TaskEntity entity = this.findById(id);

		List<UserEntity> helpers = entity.getHelperUsers();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
		Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

		helpers = helpers.stream().filter(
				(Predicate<? super UserEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
				.collect(Collectors.toList());

		Page<UserEntity> page = new PageImpl<UserEntity>(helpers, pageable, helpers.size());

		return page;
		
	}

	public TaskEntity changeState(long id, int state) {
		
		TaskEntity entity = this.findById(id);
		
		entity.setState(state);
		
		final TaskEntity updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}

	public TaskEntity addHelper(long id, long helperId) {
		// TODO Auto-generated method stub
		return null;
	}

	public TaskEntity removeHelper(long id, long helperId) {
		// TODO Auto-generated method stub
		return null;
	}

}
