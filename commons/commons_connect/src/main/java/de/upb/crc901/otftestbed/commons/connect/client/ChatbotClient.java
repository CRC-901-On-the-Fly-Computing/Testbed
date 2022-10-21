package de.upb.crc901.otftestbed.commons.connect.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import de.upb.crc901.otftestbed.commons.config.ConfigFileURLResolver;
import de.upb.crc901.otftestbed.commons.requester.DomainPreferences;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatbotClient {

	private final ConfigFileURLResolver urlResolver;

	private final RestTemplate restTemplate;


	@Autowired
	public ChatbotClient(ConfigFileURLResolver urlResolver, RestTemplate restTemplate) {
		Assert.notNull(urlResolver, "urlResolver must not be null");
		Assert.notNull(restTemplate, "restTemplate must not be null");

		this.urlResolver = urlResolver;
		this.restTemplate = restTemplate;
	}


	public DomainPreferences postText(String input) {

		// transform the data into a format that the chatbot understands
		Map<String, String> body = new HashMap<>();
		body.put("raw_text", input);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		String url = urlResolver.getB1BaseUrl() + urlResolver.getB1PostTextSuffix();

		// call the chatbot
		return restTemplate.postForEntity(url, request, DomainPreferences.class).getBody();
	}
}
