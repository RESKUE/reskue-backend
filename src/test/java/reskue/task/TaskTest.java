package reskue.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import reskue.ReskueTestInitializer;
import reskue.ReskueTestTerminator;

@SpringBootTest
@ContextConfiguration(initializers = ReskueTestInitializer.class)
@Import(ReskueTestTerminator.class)
@TestPropertySource(locations="classpath:test.properties")
@TestInstance(Lifecycle.PER_CLASS)
public class TaskTest {

	@Autowired
	private TaskController controller;
	
	@Test
	@WithMockUser(roles = { "administrator" })
	public void crud() {
		
		Page<TaskEntity> emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
		TaskEntity task = this.getDefaultEntity();
		ResponseEntity<TaskEntity> createResponse = this.controller.create(task);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(createResponse.getBody()).isNotNull();
		
		TaskEntity createdTask = createResponse.getBody();
		assertThat(createdTask.getId()).isNotNull();
		compareEntities(createdTask, task);
		
		Page<TaskEntity> nonEmptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(nonEmptyFindAll).isNotNull();
		assertThat(nonEmptyFindAll.getContent().size()).isEqualTo(1);
		
		List<TaskEntity> findAllTasks = nonEmptyFindAll.getContent();
		TaskEntity findAllTask = findAllTasks.get(0);
		assertThat(findAllTask.getId()).isEqualTo(createdTask.getId());
		
		ResponseEntity<TaskEntity> findByIdResponse = this.controller.findById(createdTask.getId());
		assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findByIdResponse.getBody()).isNotNull();
		
		TaskEntity findByIdTask = findByIdResponse.getBody();
		assertThat(findByIdTask.getId()).isEqualTo(createdTask.getId());
		compareEntities(findByIdTask, task);
		
		TaskEntity patch = this.getPatchEntity();
		ResponseEntity<TaskEntity> updateResponse = this.controller.update(createdTask.getId(), patch);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResponse.getBody()).isNotNull();
		
		TaskEntity updatedTask = updateResponse.getBody();
		assertThat(updatedTask.getId()).isEqualTo(createdTask.getId());
		compareEntities(updatedTask, patch);
		
		Map<String, Boolean> deleteResponse = this.controller.delete(createdTask.getId());
		assertThat(deleteResponse.containsKey("deleted")).isTrue();
		assertThat(deleteResponse.get("deleted")).isTrue();
		
		emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
	}
	
	private TaskEntity getDefaultEntity() {
		TaskEntity task = new TaskEntity();
		task.setName("test");
		task.setDescription("test");
		task.setPriority(0);
		task.setIsEndangered(0);
		task.setState(0);
		task.setRecommendedHelperUsers(1);
		return task;
	}
	
	private TaskEntity getPatchEntity() {
		TaskEntity patch = new TaskEntity();
		patch.setName("test1");
		patch.setDescription("test1");
		patch.setPriority(1);
		patch.setIsEndangered(1);
		patch.setState(1);
		patch.setRecommendedHelperUsers(2);
		return patch;
	}
	
	private void compareEntities(TaskEntity a, TaskEntity b) {
		
		assertThat(a.getName()).isEqualTo(b.getName());
		assertThat(a.getDescription()).isEqualTo(b.getDescription());
		assertThat(a.getPriority()).isEqualTo(b.getPriority());
		assertThat(a.getIsEndangered()).isEqualTo(b.getIsEndangered());
		assertThat(a.getState()).isEqualTo(b.getState());
		assertThat(a.getRecommendedHelperUsers()).isEqualTo(b.getRecommendedHelperUsers());
		
	}
	
}
