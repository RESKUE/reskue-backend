package kueres.query;

import kueres.utility.Utility;

public class SearchCriteria {

	private String key;
	private Object value;
	private SearchOperation operation;
	
	public SearchCriteria(String filter) {
		
		Utility.LOG.info("filter: {}", filter);
		String[] keyAndValue = filter.split(SearchOperation.getOperationSetRegex());
		Utility.LOG.info("keyAndValue: {}", (Object) keyAndValue);
		if (keyAndValue.length == 2) {
			this.key = keyAndValue[0];
			this.value = keyAndValue[1];
			Utility.LOG.info("operation: {}", filter.charAt(this.key.length()));
			this.operation = SearchOperation.getOperation(filter.charAt(this.key.length()));
			if (operation == null) {
				Utility.LOG.info("no operation in filter");
			}
			Utility.LOG.info("key: {}, value: {}, operation: {}", this.key, this.value, this.operation);
		} else {
			throw new IllegalArgumentException("incorrect filter format");
		}
		
	}
	
	public SearchCriteria(String key, Object value, SearchOperation operation) {
		this.key = key;
		this.value = value;
		this.operation = operation;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public SearchOperation getOperation() {
		return this.operation;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void setOperation(SearchOperation operation) {
		this.operation = operation;
	}
	
}
