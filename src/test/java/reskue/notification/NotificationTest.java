//package reskue.notification;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//
//import reskue.ReskueTestInitializer;
//import reskue.ReskueTestTerminator;
//
//@SpringBootTest
//@ContextConfiguration(initializers = ReskueTestInitializer.class)
//@Import(ReskueTestTerminator.class)
//@TestPropertySource(locations="classpath:test.properties")
//@TestInstance(Lifecycle.PER_CLASS)
//public class NotificationTest {
//
//	@Autowired
//	private NotificationController controller;
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void crud() {
//		
//		Page<NotificationEntity> emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
//		assertThat(emptyFindAll).isNotNull();
//		assertThat(emptyFindAll.getContent()).isEmpty();
//		
//		NotificationEntity notification = this.getDefaultEntity();
//		ResponseEntity<NotificationEntity> createResponse = this.controller.create(notification);
//		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(createResponse.getBody()).isNotNull();
//		
//		NotificationEntity createdNotification = createResponse.getBody();
//		assertThat(createdNotification.getId()).isNotNull();
//		compareEntities(createdNotification, notification);
//		
//		Page<NotificationEntity> nonEmptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
//		assertThat(nonEmptyFindAll).isNotNull();
//		assertThat(nonEmptyFindAll.getContent().size()).isEqualTo(1);
//		
//		List<NotificationEntity> findAllNotifications = nonEmptyFindAll.getContent();
//		NotificationEntity findAllNotification = findAllNotifications.get(0);
//		assertThat(findAllNotification.getId()).isEqualTo(createdNotification.getId());
//		
//		ResponseEntity<NotificationEntity> findByIdResponse = this.controller.findById(createdNotification.getId());
//		assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(findByIdResponse.getBody()).isNotNull();
//		
//		NotificationEntity findByIdNotification = findByIdResponse.getBody();
//		assertThat(findByIdNotification.getId()).isEqualTo(findByIdNotification.getId());
//		compareEntities(findByIdNotification, notification);
//		
//		Map<String, Boolean> deleteResponse = this.controller.delete(createdNotification.getId());
//		assertThat(deleteResponse.containsKey("deleted")).isTrue();
//		assertThat(deleteResponse.get("deleted")).isTrue();
//		
//		emptyFindAll = this.controller.findAll(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));
//		assertThat(emptyFindAll).isNotNull();
//		assertThat(emptyFindAll.getContent()).isEmpty();
//		
//	}
//	
//	private NotificationEntity getDefaultEntity() {
//		NotificationEntity notification = new NotificationEntity();
//		notification.setTitle("test");
//		notification.setMessage("test");
//		notification.setType(0);
//		notification.setSentAt(new Date());
//		return notification;
//	}
//	
//	private void compareEntities(NotificationEntity a, NotificationEntity b) {
//		
//		assertThat(a.getTitle()).isEqualTo(b.getTitle());
//		assertThat(a.getMessage()).isEqualTo(b.getMessage());
//		assertThat(a.getType()).isEqualTo(b.getType());
//		assertThat(a.getSentAt().compareTo(b.getSentAt())).isEqualTo(0);
//		
//	}
//	
//}
