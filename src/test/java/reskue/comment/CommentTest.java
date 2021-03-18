//package reskue.comment;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Date;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//
//import reskue.ReskueTestInitializer;
//import reskue.ReskueTestTerminator;
//import reskue.culturalasset.CulturalAssetEntity;
//import reskue.task.TaskEntity;
//
//@SpringBootTest
//@ContextConfiguration(initializers = ReskueTestInitializer.class)
//@Import(ReskueTestTerminator.class)
//@TestPropertySource(locations="classpath:test.properties")
//@TestInstance(Lifecycle.PER_CLASS)
//public class CommentTest {
//	
//	@Autowired
//	private CommentController controller;
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void crudTest() {
//				
//	}
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void applyPatchTest() {
//		
//		CommentEntity comment = this.getDefaultEntity();
//		CommentEntity patch = this.getPatchEntity();
//		
//		patch.setCommentTask(new TaskEntity());
//		comment.applyPatch(pat-ch);
//		assertThat(comment.getCommentTask()).isNotNull();
//		assertThat(comment.getCommentTask()).isEqualTo(patch.getCommentTask());
//		
//		patch.setCommentTask(null);
//		patch.setCommentCulturalAsset(new CulturalAssetEntity());
//		comment.applyPatch(patch);
//		assertThat(comment.getCommentTask()).isNull();
//		assertThat(comment.getCommentCulturalAsset()).isNotNull();
//		assertThat(comment.getCommentCulturalAsset()).isEqualTo(patch.getCommentCulturalAsset());
//		
//		patch.setCommentTask(new TaskEntity());
//		comment.applyPatch(patch);
//		assertThat(comment.getCommentTask()).isNull();
//		assertThat(comment.getCommentCulturalAsset()).isNotNull();
//		assertThat(comment.getCommentCulturalAsset()).isEqualTo(patch.getCommentCulturalAsset());
//		
//		patch.setCommentTask(null);
//		patch.setCommentCulturalAsset(null);
//		comment.applyPatch(patch);
//		assertThat(comment.getCommentTask()).isNull();
//		assertThat(comment.getCommentCulturalAsset()).isNull();
//		
//	}
//	
//	// Wait needs exception
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void dateTest() throws InterruptedException {
//		
//		CommentEntity comment = this.getDefaultEntity();
//		CommentEntity patch = this.getPatchEntity();
//		this.wait(1000);
//		patch.setCreatedAt(new Date());
//		CommentEntity updatedEntity = comment;
//		updatedEntity.applyPatch(patch);
//
//		assertThat(comment.getCreatedAt()).isEqualTo(updatedEntity.getCreatedAt());
//		assertThat(comment.getUpdatedAt()).isNotEqualTo(updatedEntity.getUpdatedAt());
//		
//	}
//	
//	private CommentEntity getDefaultEntity() {
//		CommentEntity comment = new CommentEntity();
//		comment.setText("test");
//
//		return comment;
//	}
//	
//	private CommentEntity getPatchEntity() {
//		CommentEntity patch = new CommentEntity();
//		patch.setText("test1");
//		return patch;
//	}
//	
//	private void compareEntities(CommentEntity a, CommentEntity b) {
//		
//		assertThat(a.getText()).isEqualTo(b.getText());
//		assertThat(a.getCreatedAt()).isEqualTo(b.getCreatedAt());
//		assertThat(a.getUpdatedAt()).isEqualTo(b.getUpdatedAt());
//		
//	}
//	
//}
