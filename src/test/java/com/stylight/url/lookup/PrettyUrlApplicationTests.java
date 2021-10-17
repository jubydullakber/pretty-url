package com.stylight.url.lookup;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrettyUrlApplicationTests {


	private final String PRETTY_URL = "/url/pretty";
	private final String ORIGINAL_URL = "/url/original";
	@LocalServerPort
	public int port;
	TestRestTemplate testRestTemplate = new TestRestTemplate();
	@Value("${app.base.domain}")
	private String basePath;

	@Test
	public void testValidPrettyUrl() throws JSONException {

		List<String> originalUrls = Arrays.asList("/products");
		String expected = "{" +
				"\"" + basePath + "/products\":\"" + basePath + "/Fashion/\"" +
				"}";
		ResponseEntity<String> response = testRestTemplate.exchange(
				createURLWithPort(PRETTY_URL), HttpMethod.POST, getRequestEntity(originalUrls), String.class
		);

		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void testValidOriginalUrl() throws JSONException {
		List<String> originalUrls = Arrays.asList("/Women/");
		String expected = "{" +
				"\"" + basePath + "/Women/\":\"" + basePath + "/products?gender=female\"" +
				"}";
		ResponseEntity<String> response = testRestTemplate.exchange(
				createURLWithPort(ORIGINAL_URL), HttpMethod.POST, getRequestEntity(originalUrls), String.class
		);

		JSONAssert.assertEquals(expected, response.getBody(), true);
	}


	private HttpEntity<List<String>> getRequestEntity(List<String> urls) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(urls, headers);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + "/" + uri;
	}
}
