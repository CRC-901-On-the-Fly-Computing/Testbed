package de.upb.crc901.otftestbed.otfprovider.gatekeeper.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import de.upb.crc901.otftestbed.otfprovider.gatekeeper.db.Repository;
import de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.spring_server.Swagger2SpringBoot;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableMongoRepositories(basePackageClasses = Repository.class)
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.spring_server",
		"de.upb.crc901.otftestbed.otfprovider.gatekeeper.impl" })
public class Application extends Swagger2SpringBoot {

	public static void main(String[] args) {
		System.setProperty("spring.data.mongodb.username", args[0]);
		System.setProperty("spring.data.mongodb.password", args[1]);

		new SpringApplication(Application.class).run(args);
	}
}
