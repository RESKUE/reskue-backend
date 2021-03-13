package reskue.user;

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
public class UserTest {

	@Autowired
	private UserController controller;
	
	@Test
	@WithMockUser(roles = { "administrator" })
	public void crud() {
		
		Page<UserEntity> emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
		UserEntity user = this.getDefaultEntity();
		ResponseEntity<UserEntity> createResponse = this.controller.create(user);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(createResponse.getBody()).isNotNull();
		
		UserEntity createdUser = createResponse.getBody();
		assertThat(createdUser.getId()).isNotNull();
		compareEntities(createdUser, user);
		
		Page<UserEntity> nonEmptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(nonEmptyFindAll).isNotNull();
		assertThat(nonEmptyFindAll.getContent().size()).isEqualTo(1);
		
		List<UserEntity> findAllUsers = nonEmptyFindAll.getContent();
		UserEntity findAllUser = findAllUsers.get(0);
		assertThat(findAllUser.getId()).isEqualTo(createdUser.getId());
		
		ResponseEntity<UserEntity> findByIdResponse = this.controller.findById(createdUser.getId());
		assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(findByIdResponse.getBody()).isNotNull();
		
		UserEntity findByIdUser = findByIdResponse.getBody();
		assertThat(findByIdUser.getId()).isEqualTo(createdUser.getId());
		compareEntities(findByIdUser, user);
		
		UserEntity patch = this.getPatchEntity();
		ResponseEntity<UserEntity> updateResponse = this.controller.update(createdUser.getId(), patch);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResponse.getBody()).isNotNull();
		
		UserEntity updatedUser = updateResponse.getBody();
		assertThat(updatedUser.getId()).isEqualTo(createdUser.getId());
		compareEntities(updatedUser, patch);
		
		Map<String, Boolean> deleteResponse = this.controller.delete(createdUser.getId());
		assertThat(deleteResponse.containsKey("deleted")).isTrue();
		assertThat(deleteResponse.get("deleted")).isTrue();
		
		emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
		assertThat(emptyFindAll).isNotNull();
		assertThat(emptyFindAll.getContent()).isEmpty();
		
	}
	
	private UserEntity getDefaultEntity() {
		UserEntity user = new UserEntity();
		user.setName("test");
		return user;
	}
	
	private UserEntity getPatchEntity() {
		UserEntity patch = new UserEntity();
		patch.setName("test1");
		return patch;
	}
	
	private void compareEntities(UserEntity a, UserEntity b) {
		
		assertThat(a.getName()).isEqualTo(b.getName());
		
	}
	
}
