package reskue.usergroup;

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
public class UserGroupTest {

	@Autowired
	private UserGroupController controller;
	
	@Test
	@WithMockUser(roles = { "administrator" })
	public void crud() {
		
		Page<UserGroupEntity> emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
		UserGroupEntity userGroup = this.getDefaultEntity();
		ResponseEntity<UserGroupEntity> createResponse = this.controller.create(userGroup);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(createResponse.getBody()).isNotNull();
		
		UserGroupEntity createdUserGroup = createResponse.getBody();
		assertThat(createdUserGroup.getId()).isNotNull();
		compareEntities(createdUserGroup, userGroup);
		
		Page<UserGroupEntity> nonEmptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(nonEmptyFindAll).isNotNull();
		assertThat(nonEmptyFindAll.getContent().size()).isEqualTo(1);
		
		List<UserGroupEntity> findAllUserGroups = nonEmptyFindAll.getContent();
		UserGroupEntity findAllUserGroup = findAllUserGroups.get(0);
		assertThat(findAllUserGroup.getId()).isEqualTo(createdUserGroup.getId());
		
		ResponseEntity<UserGroupEntity> findByIdResponse = this.controller.findById(createdUserGroup.getId());
		assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findByIdResponse.getBody()).isNotNull();
		
		UserGroupEntity findByIdUserGroup = findByIdResponse.getBody();
		assertThat(findByIdUserGroup.getId()).isEqualTo(createdUserGroup.getId());
		compareEntities(findByIdUserGroup, userGroup);
		
		UserGroupEntity patch = this.getPatchEntity();
		ResponseEntity<UserGroupEntity> updateResponse = this.controller.update(createdUserGroup.getId(), patch);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResponse.getBody()).isNotNull();
		
		UserGroupEntity updatedUserGroup = updateResponse.getBody();
		assertThat(updatedUserGroup.getId()).isEqualTo(createdUserGroup.getId());
		compareEntities(updatedUserGroup, patch);
		
		Map<String, Boolean> deleteResponse = this.controller.delete(createdUserGroup.getId());
		assertThat(deleteResponse.containsKey("deleted")).isTrue();
		assertThat(deleteResponse.get("deleted")).isTrue();
		
		emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
	}
	
	private UserGroupEntity getDefaultEntity() {
		UserGroupEntity userGroup = new UserGroupEntity();
		userGroup.setName("test");
		return userGroup;
	}
	
	private UserGroupEntity getPatchEntity() {
		UserGroupEntity patch = new UserGroupEntity();
		patch.setName("test1");
		return patch;
	}
	
	private void compareEntities(UserGroupEntity a, UserGroupEntity b) {
		
		assertThat(a.getName()).isEqualTo(b.getName());
		
	}
	
}
