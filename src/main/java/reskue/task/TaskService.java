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

/**
 * 
 * The TaskService provides services needed by the TaskController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Service
public class TaskService extends ReskueService<TaskEntity, TaskRepository>{
	
	/**
	 * The UserService needed to add users as helpers to the task.
	 */
	@Autowired
	protected UserService userService;
	
	/**
	 * The EntityManager needed to create a CriteriaBuilder.
	 */
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = TaskController.ROUTE;
		this.routingKey = TaskController.ROUTE;
	}
	
	/**
	 * Get all subtasks of the task.
	 * 
	 * @param id - the task's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
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
	
	/**
	 * Get all helper users of the task.
	 * 
	 * @param id - the task's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
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
	
	/**
	 * Changes the state of a task.
	 * 
	 * @param id - the task's identifier.
	 * @param state - the new state of the task.
	 * @return The task after the state was changed.
	 */
	public TaskEntity changeState(long id, int state) {
		
		Utility.LOG.trace("TaskService.changeState called.");
		
		TaskEntity entity = this.findById(id);
		
		entity.setState(state);
		
		final TaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("TaskService.changeState", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}
	
	/**
	 * Adds a helper user to the task.
	 * 
	 * @param id - the task's identifier.
	 * @param helperId - the helper user's identifier.
	 * @return The task after the helper user was added.
	 */
	public TaskEntity addHelper(long id, long helperId) {
		
		Utility.LOG.trace("TaskService.addHelper called.");
		
		TaskEntity entity = this.findById(id);
		UserEntity helper = userService.findById(helperId);		
		List<UserEntity> newHelpers = entity.getHelperUsers();
		
		//if the helper is already a helper
		if(newHelpers.contains(helper)) {
			return entity;
		} else {
			newHelpers.add(helper);
			entity.setHelperUsers(newHelpers);
		}
		
		final TaskEntity updatedEntity = repository.save(entity);
		
		EventConsumer.sendEvent("TaskService.addHelper", EventType.UPDATE.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(updatedEntity));
		
		return updatedEntity;
		
	}
	
	/**
	 * Removes a helper user from the task.
	 * 
	 * @param id - the task's identifier.
	 * @param helperId - the helper user's identifier.
	 * @return The task after the helper user was removed.
	 */
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
