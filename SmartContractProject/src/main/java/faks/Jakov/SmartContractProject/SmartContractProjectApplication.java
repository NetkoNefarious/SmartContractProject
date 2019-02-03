package faks.Jakov.SmartContractProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartContractProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartContractProjectApplication.class, args);
	}
	
	@Bean
	public Logger getLogger() {
		return LoggerFactory.getLogger(SmartContractProjectApplication.class);
	}
}