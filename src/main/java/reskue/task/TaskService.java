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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;
import reskue.ReskueService;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;
import reskue.user.UserService;

@Service
public class TaskService extends ReskueService<TaskEntity, TaskRepository>{
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	protected UserService userService;

	@Override
	@PostConstruct
	public void init() {
		this.identifier = TaskController.ROUTE;
		this.routingKey = TaskController.ROUTE;
	}

	@SuppressWarnings("unchecked")
	public Page<SubtaskEntity> getAllSubtasks(long id, EntitySpecification<SubtaskEntity> specification,
			Pageable pageable) {	
		
		Utility.LOG.trace("TaskService.getAllSubtasks called.");
		
		TaskEntity entity = this.findById(id);
		List<SubtaskEntity> subtasks = entity.getSubtasks();

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<SubtaskEntity> criteriaQuery = criteriaBuilder.createQuery(SubtaskEntity.class);
			Root<SubtaskEntity> root = criteriaQuery.from(SubtaskEntity.class);

			subtasks = subtasks.stream().filter(
					(Predicate<? super SubtaskEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<SubtaskEntity> page = new PageImpl<SubtaskEntity>(subtasks, pageable, subtasks.size());
		
		EventConsumer.sendEvent("TaskService.getAllSubtasks", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}

	@SuppressWarnings("unchecked")
	public Page<UserEntity> getAllHelpers(long id, EntitySpecification<UserEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("TaskService.getAllHelpers called.");
		
		TaskEntity entity = this.findById(id);
		List<UserEntity> helpers = entity.getHelperUsers();

		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
			Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

			helpers = helpers.stream().filter(
					(Predicate<? super UserEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<UserEntity> page = new PageImpl<UserEntity>(helpers, pageable, helpers.size());
		
		EventConsumer.sendEvent("TaskService.getAllHelpers", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}

	public TaskEntity changeState(long id, int state) {
		
		Utility.LOG.trace("TaskService.changeState called.");
		
		TaskEntity entity = this.findById(id);
		
		entity.setState(state);
		
		final TaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("TaskService.changeState", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}

	public TaskEntity addHelper(long id, long helperId) {
		
		Utility.LOG.trace("TaskService.addHelper called.");
		
		TaskEntity entity = this.findById(id);
		UserEntity helper = userService.findById(helperId);		
		List<UserEntity> newHelpers = entity.getHelperUsers();
		
		//if the helper is already a helper
		if(newHelpers.contains(helper)) {
			newHelpers.add(helper);
			entity.setHelperUsers(newHelpers);
		} else {
			return entity;
		}
		
		final TaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("TaskService.addHelper", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}

	public TaskEntity removeHelper(long id, long helperId) {
		
		Utility.LOG.trace("TaskService.removeHelper called.");
		
		TaskEntity entity = this.findById(id);
		UserEntity helper = userService.findById(helperId);	
		List<UserEntity> newHelpers = entity.getHelperUsers();
		
		//if the helper is actually a helper
		if(newHelpers.contains(helper)) {
			newHelpers.remove(helper);
			entity.setHelperUsers(newHelpers);
		} else {
			return entity;
		}
		
		final TaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("TaskService.removeHelper", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}

}
