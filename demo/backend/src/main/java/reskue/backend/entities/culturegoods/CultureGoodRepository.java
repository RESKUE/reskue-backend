package reskue.backend.entities.culturegoods;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultureGoodRepository extends JpaRepository<CultureGood, Long>{

	List<CultureGood> findByNameOrderById(String name);
	
}