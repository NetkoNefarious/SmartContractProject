package faks.Jakov.SmartContractProject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import faks.Jakov.SmartContractProject.Service.StorageProperties;
import faks.Jakov.SmartContractProject.Service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SmartContractProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartContractProjectApplication.class, args);
	}
	
	@Bean
	public Logger getLogger() {
		return LoggerFactory.getLogger(SmartContractProjectApplication.class);
	}
	
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}