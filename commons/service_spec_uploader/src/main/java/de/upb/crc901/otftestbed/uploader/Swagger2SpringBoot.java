package de.upb.crc901.otftestbed.uploader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import de.upb.crc901.otftestbed.uploader.api.ServiceSpecificationRepositiory;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
@EnableMongoRepositories(basePackageClasses=ServiceSpecificationRepositiory.class)
@ComponentScan(basePackages = { "de.upb.crc901.otftestbed.uploader.config", "de.upb.crc901.otftestbed.uploader.api" })
public class Swagger2SpringBoot implements CommandLineRunner {

	
    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
		System.setProperty("spring.data.mongodb.username", args[0]);
		System.setProperty("spring.data.mongodb.password", args[1]);
		new SpringApplication(Swagger2SpringBoot.class).run(args);
}

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
