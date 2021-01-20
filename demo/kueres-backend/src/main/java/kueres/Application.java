package kueres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import kueres.media.MediaConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MediaConfigurationProperties.class)
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}