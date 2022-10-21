package de.upb.crc901.otftestbed.review_board.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import de.upb.crc901.otftestbed.commons.connect.EnablePocConnector;
import de.upb.crc901.otftestbed.commons.environment.Detector;
import de.upb.crc901.otftestbed.commons.telemetry.EnablePocTelemetry;
import de.upb.crc901.otftestbed.review_board.generated.spring_server.Swagger2SpringBoot;
import de.upb.crc901.otftestbed.review_board.impl.repositories.ReviewRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnablePocConnector
//@EnablePocTelemetry
@EnableSwagger2
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableMongoRepositories(basePackageClasses = ReviewRepository.class)
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.review_board.generated.spring_server",
		"de.upb.crc901.otftestbed.review_board.impl" })
public class Application extends Swagger2SpringBoot {

	public static void main(String[] args) {
		System.getProperties().setProperty("spring.profiles.active", Detector.detectProfile().toString());

		new SpringApplication(Application.class).run(args);
	}
}
