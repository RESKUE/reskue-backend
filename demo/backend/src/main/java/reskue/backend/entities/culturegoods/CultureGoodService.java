package reskue.backend.entities.culturegoods;

import java.util.List;

import org.springframework.stereotype.Service;

import reskue.backend.entities.BaseService;

@Service
public class CultureGoodService extends BaseService<CultureGood, CultureGoodRepository> {

	public List<CultureGood> findByName(String name) {
		
		return repository.findByNameOrderById(name);
		
	}
	
}
