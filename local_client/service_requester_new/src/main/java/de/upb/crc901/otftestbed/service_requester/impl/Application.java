package de.upb.crc901.otftestbed.service_requester.impl;

import de.upb.crc901.otftestbed.commons.connect.EnablePocConnector;
import de.upb.crc901.otftestbed.commons.environment.Detector;
import de.upb.crc901.otftestbed.service_requester.generated.spring_server.Swagger2SpringBoot;
import de.upb.crc901.otftestbed.service_requester.impl.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnablePocConnector
@EnableSwagger2
@EnableConfigurationProperties(StorageProperties.class)
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.service_requester.generated.spring_server",
		"de.upb.crc901.otftestbed.service_requester.impl" })
public class Application extends Swagger2SpringBoot {

	public static void main(String[] args) {
		System.getProperties().setProperty("spring.profiles.active", Detector.detectProfile().toString());

		new SpringApplication(Application.class).run(args);
	}
}
