package reskue.comment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
public class CommentTest {
	
	@Autowired
	private CommentController controller;
	
	@Test
	@WithMockUser(roles = { "administrator" })
	public void crud() {
		
		CommentEntity comment = new CommentEntity();
		
		ResponseEntity<CommentEntity> responseCreate = this.controller.create(comment);
		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseCreate.getBody()).isNotNull();
	
		//TODO: complete test
		
	}

}
