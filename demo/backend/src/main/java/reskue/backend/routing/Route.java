package reskue.backend.routing;

public class Route {

	private String identifier;
	private boolean needsAuthorization;
	
	public Route(String identifier, boolean needsAuthorization) {
		this.identifier = identifier;
		this.needsAuthorization = needsAuthorization;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String name) {
		this.identifier = name;
	}
	
	public boolean needsAuthorization() {
		return needsAuthorization;
	}
	
	public void setNeedsAuthorization(boolean needsAuthorization) {
		this.needsAuthorization = needsAuthorization;
	}
	
	
	
}
