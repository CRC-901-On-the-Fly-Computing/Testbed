package de.upb.crc901.otftestbed.commons.telemetry;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryConfiguration;

/**
 * Annotation to enable the {@link de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService TelemetryService} in a
 * spring context.
 *
 * @author Roman
 */
@Configuration
@Import(TelemetryConfiguration.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnablePocTelemetry {
}
