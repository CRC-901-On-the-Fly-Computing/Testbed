package de.upb.crc901.otftestbed.commons.telemetry.es;

import java.net.InetSocketAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@PropertySource("classpath:/urlconfigs/config-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "elasticsearch")
@EnableConfigurationProperties(TelemetryConfiguration.class)
@ComponentScan("de.upb.crc901.otftestbed.commons.telemetry.es")
@EnableElasticsearchRepositories(basePackages = "de.upb.crc901.otftestbed.commons.telemetry.es")
public class TelemetryConfiguration{

	private String clustername;

	private String transportHost;

	private int transportPort;


	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}


	public String getTransportHost() {
		return transportHost;
	}

	public void setTransportHost(String transportHost) {
		this.transportHost = transportHost;
	}


	public int getTransportPort() {
		return transportPort;
	}

	public void setTransportPort(int transportPort) {
		this.transportPort = transportPort;
	}


	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() {
		return new ElasticsearchTemplate(getTransportClient());
	}

	@Bean
	public TransportClient getTransportClient() {
		Settings settings = Settings.builder()
				.put("cluster.name", clustername)
				.put("client.transport.sniff", false)
//				.put("client.transport.ping_timeout", "30s")
//				.put("client.transport.ignore_cluster_name", true)
				.build();

		TransportClient tc = new PreBuiltTransportClient(settings);
		tc.addTransportAddress(new TransportAddress(new InetSocketAddress(transportHost, transportPort)));

		return tc;
	}
}
