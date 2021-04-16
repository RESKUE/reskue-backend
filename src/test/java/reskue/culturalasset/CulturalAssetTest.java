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
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//
////import reskue.ReskueTestInitializer;
////import reskue.ReskueTestTerminator;
//
//@SpringBootTest
////@ContextConfiguration(initializers = ReskueTestInitializer.class)
////@Import(ReskueTestTerminator.class)
////@TestPropertySource(locations="classpath:test.properties")
//@TestInstance(Lifecycle.PER_CLASS)
//public class CulturalAssetTest {
//
//	@Autowired
//	private CulturalAssetService service;
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void testHierarchyLoopProtection() {
//		
//		CulturalAssetEntity parent = new CulturalAssetEntity();
//		parent = service.create(parent);
//		
//		CulturalAssetEntity child1 = new CulturalAssetEntity();
//		child1 = service.create(child1);
//		
//		CulturalAssetEntity child2 = new CulturalAssetEntity();
//		child2 = service.create(child2);
//		
//		CulturalAssetEntity child3 = new CulturalAssetEntity();
//		child3 = service.create(child3);
//		
//		service.addCulturalAssetChild(parent.getId(), child1.getId());
//		service.addCulturalAssetChild(child1.getId(), child2.getId());
//		service.addCulturalAssetChild(child2.getId(), child3.getId());
//		service.addCulturalAssetChild(child3.getId(), parent.getId());
//		
//		parent = service.findById(parent.getId());
//		child1 = service.findById(child1.getId());
//		child2 = service.findById(child2.getId());
//		child3 = service.findById(child3.getId());
//		
//		assertThat(parent.getCulturalAssetChildren()).isNotNull();
//		assertThat(child1.getCulturalAssetParent()).isNotNull();
//		assertThat(child1.getCulturalAssetChildren()).isNotNull();
//		assertThat(child2.getCulturalAssetParent()).isNotNull();
//		assertThat(child2.getCulturalAssetChildren()).isNotNull();
//		assertThat(child3.getCulturalAssetParent()).isNotNull();
//		
//		assertThat(child3.getCulturalAssetChildren()).isNull();
//		assertThat(parent.getCulturalAssetParent()).isNull();
//		
//		assertThat(parent.getLevel()).isEqualTo(0);
//		assertThat(child1.getLevel()).isEqualTo(1);
//		assertThat(child2.getLevel()).isEqualTo(2);
//		assertThat(child3.getLevel()).isEqualTo(3);
//		
//	}
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void testHierarchyHeightProtection() {
//		
//		CulturalAssetEntity parent = new CulturalAssetEntity();
//		parent = service.create(parent);
//		
//		CulturalAssetEntity child1 = new CulturalAssetEntity();
//		child1 = service.create(child1);
//		
//		CulturalAssetEntity child2 = new CulturalAssetEntity();
//		child2 = service.create(child2);
//		
//		CulturalAssetEntity child3 = new CulturalAssetEntity();
//		child3 = service.create(child3);
//		
//		CulturalAssetEntity child4 = new CulturalAssetEntity();
//		child4 = service.create(child4);
//		
//		CulturalAssetEntity child5 = new CulturalAssetEntity();
//		child5 = service.create(child5);
//		
//		service.addCulturalAssetChild(parent.getId(), child1.getId());
//		service.addCulturalAssetChild(child1.getId(), child2.getId());
//		service.addCulturalAssetChild(child2.getId(), child3.getId());
//		service.addCulturalAssetChild(child3.getId(), child4.getId());
//		service.addCulturalAssetChild(child4.getId(), child5.getId());
//		
//		parent = service.findById(parent.getId());
//		child1 = service.findById(child1.getId());
//		child2 = service.findById(child2.getId());
//		child3 = service.findById(child3.getId());
//		child4 = service.findById(child4.getId());
//		child5 = service.findById(child5.getId());
//		
//		assertThat(parent.getCulturalAssetChildren()).isNotNull();
//		assertThat(child1.getCulturalAssetParent()).isNotNull();
//		assertThat(child1.getCulturalAssetChildren()).isNotNull();
//		assertThat(child2.getCulturalAssetParent()).isNotNull();
//		assertThat(child2.getCulturalAssetChildren()).isNotNull();
//		assertThat(child3.getCulturalAssetParent()).isNotNull();
//		
//		assertThat(child3.getCulturalAssetChildren()).isNull();
//		assertThat(child4.getCulturalAssetParent()).isNull();
//		
//		assertThat(child4.getCulturalAssetChildren()).isNotNull();
//		assertThat(child5.getCulturalAssetParent()).isNotNull();
//		
//		assertThat(child5.getCulturalAssetChildren()).isNull();
//		
//		assertThat(parent.getLevel()).isEqualTo(0);
//		assertThat(child1.getLevel()).isEqualTo(1);
//		assertThat(child2.getLevel()).isEqualTo(2);
//		assertThat(child3.getLevel()).isEqualTo(3);
//		
//		assertThat(child4.getLevel()).isEqualTo(0);
//		assertThat(child5.getLevel()).isEqualTo(1);
//		
//	}
//	
//	@Test
//	@WithMockUser(roles = { "administrator" })
//	public void testUpdateIsEndangered() {
//		
//		CulturalAssetEntity parent = new CulturalAssetEntity();
//		parent = service.create(parent);
//		
//		CulturalAssetEntity child1 = new CulturalAssetEntity();
//		child1 = service.create(child1);
//		
//		CulturalAssetEntity child2 = new CulturalAssetEntity();
//		child2 = service.create(child2);
//		
//		service.addCulturalAssetChild(parent.getId(), child1.getId());
//		service.addCulturalAssetChild(child1.getId(), child2.getId());
//		
//		service.updateIsEndangered(parent, 1);
//		
//		parent = service.findById(parent.getId());
//		child1 = service.findById(child1.getId());
//		child2 = service.findById(child2.getId());
//		
//		assertThat(parent.getCulturalAssetChildren()).isNotNull();
//		assertThat(child1.getCulturalAssetParent()).isNotNull();
//		assertThat(child1.getCulturalAssetChildren()).isNotNull();
//		assertThat(child2.getCulturalAssetParent()).isNotNull();
//		assertThat(child2.getCulturalAssetChildren()).isNotNull();
//		
//		assertThat(parent.getIsEndangered()).isEqualTo(1);
//		assertThat(child1.getIsEndangered()).isEqualTo(1);
//		assertThat(child2.getIsEndangered()).isEqualTo(1);
//		
//	}
//	
//}