package kueres.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import kueres.utility.Utility;

public class SortBuilder {

	public static Sort buildSort(String[] params) {
		Utility.LOG.info("buildSort");
		return Sort.by(assembleOrders(params));
		
	}
	
	private static List<Order> assembleOrders(String[] params) {
		Utility.LOG.info("assembleSort");
		List<Order> orders = new ArrayList<Order>();
		for (String parameter : params) {
			Utility.LOG.info("parameter: {}", parameter);
			String[] typeAndDirection = parameter.split(",");
			Utility.LOG.info("length: {}", typeAndDirection.length);
			if (typeAndDirection.length == 2) {
				Utility.LOG.info("getDirection: {}", getDirection(typeAndDirection[1]));
				if (getDirection(typeAndDirection[1]) != null) {
					Utility.LOG.info("direction: {}, property: {}", getDirection(typeAndDirection[1]), typeAndDirection[1]);
					orders.add(new Order(getDirection(typeAndDirection[1]), typeAndDirection[0]));
				}
				
			}
			
		}
		
		return orders;
		
	}
	
	private static Sort.Direction getDirection(String direction) {
		Utility.LOG.info("direction: {}", direction);
		if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		} else if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		}
		return null;
	}
	
}
