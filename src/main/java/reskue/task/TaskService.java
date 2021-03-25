package reskue.task;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import reskue.comment.CommentEntity;
import reskue.subtask.SubtaskEntity;
import reskue.user.UserEntity;
import reskue.user.UserService;

/**
 * 
 * The TaskService provides services needed by the TaskController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
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
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = TaskController.ROUTE;
		this.routingKey = TaskController.ROUTE;
	}
	
	/**
	 * Get all comments of the task.
	 * 
	 * @param id - the task's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<CommentEntity> getAllComments(Long id, EntitySpecification<CommentEntity> specification,
			Pageable pageable) {

		TaskEntity entity = this.findById(id);

		List<CommentEntity> comments = entity.getComments();

		if (specification != null) {	
			
			comments = comments.stream().filter(specification.toPredicate(CommentEntity.class)).collect(Collectors.toList());
			
		}

		Page<CommentEntity> page = new PageImpl<CommentEntity>(comments, pageable, comments.size());
		
		EventConsumer.sendEvent("TaskService.getAllComments", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
	/**
	 * Get all subtasks of the task.
	 * 
	 * @param id - the task's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<SubtaskEntity> getAllSubtasks(Long id, EntitySpecification<SubtaskEntity> specification,
			Pageable pageable) {	
		
		Utility.LOG.trace("TaskService.getAllSubtasks called.");
		
		TaskEntity entity = this.findById(id);
		List<SubtaskEntity> subtasks = entity.getSubtasks();

		if (specification != null) {

			subtasks = subtasks.stream().filter(specification.toPredicate(SubtaskEntity.class)).collect(Collectors.toList());
			
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
	public Page<UserEntity> getAllHelpers(Long id, EntitySpecification<UserEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("TaskService.getAllHelpers called.");
		
		TaskEntity entity = this.findById(id);
		List<UserEntity> helpers = entity.getHelperUsers();

		if (specification != null) {

			helpers = helpers.stream().filter(specification.toPredicate(UserEntity.class)).collect(Collectors.toList());
			
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
	public TaskEntity changeState(Long id, int state) {
		
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
	public TaskEntity addHelper(Long id, Long helperId) {
		
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
	public TaskEntity removeHelper(Long id, Long helperId) {
		
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
