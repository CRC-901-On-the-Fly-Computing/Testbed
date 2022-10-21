package de.upb.crc901.otftestbed.commons.connect;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Annotation to enable the {@link PocConnector} in a spring context.
 *
 * @author Roman
 */
@Configuration
@Import(PocConnectorConfiguration.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnablePocConnector {
}
