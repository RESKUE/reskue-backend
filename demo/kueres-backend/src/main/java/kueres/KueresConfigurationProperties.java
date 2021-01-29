package kueres;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kueres")
public class KueresConfigurationProperties {

	private String api;
	private String eventRoute;
	
	public String getApi() {
		return api;
	}
	
	public void setApi(String api) {
		this.api = api;
	}
	
	public String getEventRoute() {
		return this.eventRoute;
	}
	
	public void setEventRoute(String eventroute) {
		this.eventRoute = eventroute;
	}
	
}
