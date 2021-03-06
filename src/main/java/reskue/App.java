package reskue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * The main class for RESKUE.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

@SpringBootApplication
@ComponentScan("kueres")
@ComponentScan("reskue")
public class App {
	
	/**
	 * The main entrypoint for RESKUE.
	 * @param args - arguments for Spring Boot
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
