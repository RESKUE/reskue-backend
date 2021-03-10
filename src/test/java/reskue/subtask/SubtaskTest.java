package reskue.subtask;

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
public class SubtaskTest {

	@Autowired
	private SubtaskController controller;
	
	@Test
	@WithMockUser(roles = { "administrator" })
	public void crud() {
		
		Page<SubtaskEntity> emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
		SubtaskEntity subtask = this.getDefaultEntity();
		ResponseEntity<SubtaskEntity> createResponse = this.controller.create(subtask);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(createResponse.getBody()).isNotNull();
		
		SubtaskEntity createdSubtask = createResponse.getBody();
		assertThat(createdSubtask.getId()).isNotNull();
		compareEntities(createdSubtask, subtask);
		
		Page<SubtaskEntity> nonEmptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(nonEmptyFindAll).isNotNull();
		assertThat(nonEmptyFindAll.getContent().size()).isEqualTo(1);
		
		List<SubtaskEntity> findAllSubtasks = nonEmptyFindAll.getContent();
		SubtaskEntity findAllSubtask = findAllSubtasks.get(0);
		assertThat(findAllSubtask.getId()).isEqualTo(createdSubtask.getId());
		
		ResponseEntity<SubtaskEntity> findByIdResponse = this.controller.findById(createdSubtask.getId());
		assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findByIdResponse.getBody()).isNotNull();
		
		SubtaskEntity findByIdSubtask = findByIdResponse.getBody();
		assertThat(findByIdSubtask.getId()).isEqualTo(createdSubtask.getId());
		compareEntities(findByIdSubtask, subtask);
		
		SubtaskEntity patch = this.getPatchEntity();
		ResponseEntity<SubtaskEntity> updateResponse = this.controller.update(createdSubtask.getId(), patch);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResponse.getBody()).isNotNull();
		
		SubtaskEntity updatedSubtask = updateResponse.getBody();
		assertThat(updatedSubtask.getId()).isEqualTo(createdSubtask.getId());
		compareEntities(updatedSubtask, patch);
		
		Map<String, Boolean> deleteResponse = this.controller.delete(createdSubtask.getId());
		assertThat(deleteResponse.containsKey("deleted")).isTrue();
		assertThat(deleteResponse.get("deleted")).isTrue();
		
		emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
	}
	
	private SubtaskEntity getDefaultEntity() {
		SubtaskEntity subtask = new SubtaskEntity();
		subtask.setState(0);
		subtask.setText("test");
		subtask.setIsRequired(false);
		return subtask;
	}
	
	private SubtaskEntity getPatchEntity() {
		SubtaskEntity patch = new SubtaskEntity();
		patch.setState(1);
		patch.setText("test1");
		patch.setIsRequired(true);
		return patch;
	}
	
	private void compareEntities(SubtaskEntity a, SubtaskEntity b) {
		
		assertThat(a.getState()).isEqualTo(b.getState());
		assertThat(a.getText()).isEqualTo(b.getText());
		assertThat(a.getIsRequired()).isEqualTo(b.getIsRequired());
		
	}
	
}
