package de.upb.crc901.otftestbed.admin_client.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import de.upb.crc901.otftestbed.admin_client.generated.spring_server.Swagger2SpringBoot;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {
		"de.upb.crc901.otftestbed.admin_client.generated.spring_server",
		"de.upb.crc901.otftestbed.admin_client.impl" })
public class Application extends Swagger2SpringBoot {

	public static void main(String[] args) {
		new SpringApplication(Application.class).run(args);
	}
}
