package kueres.entities.test;

import org.springframework.stereotype.Service;

@Service
public class LocationService implements LocationServiceInterface {

	@Override
	public String get() {
		return "Hello World!";
	}

	
	
}
