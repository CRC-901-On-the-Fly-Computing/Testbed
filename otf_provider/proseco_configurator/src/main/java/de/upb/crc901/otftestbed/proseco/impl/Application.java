package de.upb.crc901.otftestbed.proseco.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import de.upb.crc901.otftestbed.commons.connect.EnablePocConnector;
import de.upb.crc901.otftestbed.commons.environment.Detector;
import de.upb.crc901.otftestbed.proseco.generated.spring_server.Swagger2SpringBoot;
import de.upb.crc901.otftestbed.proseco.impl.repository.StoredMMSources;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnablePocConnector
@EnableSwagger2
@EnableMongoRepositories(basePackageClasses = StoredMMSources.class)
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.proseco.generated.spring_server",
		"de.upb.crc901.otftestbed.proseco.impl",
		"de.upb.crc901.otftestbed.proseco.utils" })
public class Application extends Swagger2SpringBoot {

	public static void main(String[] args) {
		System.getProperties().setProperty("spring.profiles.active", Detector.detectProfile().toString());

		new SpringApplication(Application.class).run(args);
	}
}
