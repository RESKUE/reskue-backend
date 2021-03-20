//package reskue.culturalasset;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
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
//public class CulturalAssetTest {
//
//	@Autowired
//	private CulturalAssetController controller;
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void crud() {
//		
//		CulturalAssetEntity culturalAsset = new CulturalAssetEntity();
//		culturalAsset.setName("Test");
//		culturalAsset.setDescription("A description");
//		culturalAsset.setPriority(0);
//		culturalAsset.setIsEndangered(0);
//		culturalAsset.setAddress("White House, 1600, Pennsylvania Avenue Northwest, Washington, District of Columbia, 20500, United States");
//		culturalAsset.setLongitude(38.897699700000004);
//		culturalAsset.setLatitude(-77.03655315);
//		
//		ResponseEntity<CulturalAssetEntity> responseCreate = this.controller.create(culturalAsset);
//		assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(responseCreate.getBody()).isNotNull();
//	
//		//TODO: complete test
//		
//	}
//	
//}
