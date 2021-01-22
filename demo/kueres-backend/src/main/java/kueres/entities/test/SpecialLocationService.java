package kueres.entities.test;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SpecialLocationService implements LocationServiceInterface {

	@Override
	public String get() {
		return "custom implementation";
	}

}
