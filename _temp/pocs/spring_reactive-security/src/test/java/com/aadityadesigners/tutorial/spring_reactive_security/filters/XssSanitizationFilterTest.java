package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test class for {@link XssSanitizationFilter}.
 */
public class XssSanitizationFilterTest {

  private XssSanitizationFilter filter;
  private WebFilterChain filterChain;

  /**
   * Set up the test environment before each test.
   */
  @BeforeEach
  public void setUp() {
    filter = new XssSanitizationFilter();
    filterChain = mock(WebFilterChain.class);
    when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Flux.empty().then());
  }

  /**
   * Test that the filter sanitizes JSON input.
   */
  @Test
  public void testSanitizePlainJson() {
    String plainJson      = "{\"id\":\"ab0c3a7cc79c2ad5ffaa2600c6de2fdff3409f07d50c06350d6cdef52bd3c4c9\",\"name\":\"Summer Sale\",\"budget\":5000,\"status\":\"Active\",\"startDate\":\"2024-03-24\",\"endDate\":\"2024-04-24\",\"targetAudience\":[\"Young Adults\"],\"promotion\":{\"promoCode\":\"abcd-efgh-ijkl-mnop\",\"description\":\"20% off on all products\",\"redemptionCode\":\"U2FsdGVkX1+8Jv3FZg+8WpR/3b9aaF7zFgxQeAGtztc=\"},\"offerConstruct\":{\"type\":\"Discount\",\"description\":\"Get 20% off on all products\",\"keywords\":[\"summer\",\"sale\",\"discount\"]},\"termsAndConditions\":{\"summary\":\"Terms apply\",\"fullText\":\"Full terms and conditions text\"}}";
    String sanitizedJson  = "{\"id\":\"ab0c3a7cc79c2ad5ffaa2600c6de2fdff3409f07d50c06350d6cdef52bd3c4c9\",\"name\":\"Summer Sale\",\"budget\":5000,\"status\":\"Active\",\"startDate\":\"2024-03-24\",\"endDate\":\"2024-04-24\",\"targetAudience\":[\"Young Adults\"],\"promotion\":{\"promoCode\":\"abcd-efgh-ijkl-mnop\",\"description\":\"20% off on all products\",\"redemptionCode\":\"U2FsdGVkX1+8Jv3FZg+8WpR/3b9aaF7zFgxQeAGtztc=\"},\"offerConstruct\":{\"type\":\"Discount\",\"description\":\"Get 20% off on all products\",\"keywords\":[\"summer\",\"sale\",\"discount\"]},\"termsAndConditions\":{\"summary\":\"Terms apply\",\"fullText\":\"Full terms and conditions text\"}}";
    String result = getFilteredValue(plainJson);

    // Verify that the request body was sanitized
    assertEquals(sanitizedJson, result);
  }

  /**
   * Test that the filter sanitizes JSON input with scripted code.
   */
  @Test
  public void testSanitizeJsonWithScriptedCode() {
    String jsonWithScript ="{\n"
        + "    \"name\": \"Summer <script>alert('XSS')</script> Sale\",\n"
        + "    \"budget\": 5000,\n"
        + "    \"status\": \"Active\",\n"
        + "    \"targetAudience\": [\"Young Adults\"],\n"
        + "    \"promotion\": {\n"
        + "        \"promoCode\": \"abcd-<img src=x onerror=alert('XSS')>-efgh-ijkl-mnop\",\n"
        + "        \"description\": \"20% off on all products <a href='javascript:alert(\\\"XSS\\\")'>Click here</a>\"\n"
        + "    }\n"
        + "}\n";
    String sanitizedJson = "{"
        + "\"name\":\"Summer  Sale\","
        + "\"budget\":5000,"
        + "\"status\":\"Active\","
        + "\"targetAudience\":[\"Young Adults\"],"
        + "\"promotion\":{"
        + "\"promoCode\":\"abcd--efgh-ijkl-mnop\","
        + "\"description\":\"20% off on all products Click here\""
        + "}"
        + "}";

    String result = getFilteredValue(jsonWithScript);

    // Verify that the request body was sanitized
    assertEquals(sanitizedJson, result);
  }

  /**
   * Test that the filter sanitizes JSON input with special characters.
   */
  @Test
  public void testSanitizeJsonWithSpecialCharacters() {
    String jsonWithSpecialCharacters = "{\n"
        + "    \"name\": \"Summer & Winter Sale > 50%\",\n"
        + "    \"budget\": 5000,\n"
        + "    \"status\": \"Active <script>alert('Special Characters')</script>\",\n"
        + "    \"targetAudience\": [\"Young Adults\", \"Teens & Tweens\"],\n"
        + "    \"promotion\": {\n"
        + "        \"promoCode\": \"abcd-<img src=x onerror=alert('XSS')>-efgh-ijkl-mnop\",\n"
        + "        \"description\": \"20% off on all products & get an extra 10% off with code '<>'\"\n"
        + "    }\n"
        + "}\n";
    String sanitizedJson = "{"
        + "\"name\":\"Summer &amp;amp; Winter Sale &amp;gt; 50%\","
        + "\"budget\":5000,"
        + "\"status\":\"Active\","
        + "\"targetAudience\":[\"Young Adults\",\"Teens & Tweens\"],"
        + "\"promotion\":{"
        + "\"promoCode\":\"abcd--efgh-ijkl-mnop\","
        + "\"description\":\"20% off on all products &amp;amp; get an extra 10% off with code '&amp;lt;&amp;gt;'\""
        + "}"
        + "}";
    String result = getFilteredValue(jsonWithSpecialCharacters);

    // Verify that the request body was sanitized
    assertEquals(sanitizedJson, result);
  }

  /**
   * Test that the filter sanitizes JSON input with unwanted characters.
   *
   * @param value the value to sanitize
   * @return the sanitized value
   */
  private String getFilteredValue(String value) {
    // Create a mock ServerHttpRequest with the given value
    ServerHttpRequest request = MockServerHttpRequest
        .post("/")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Flux.just(stringToDataBuffer(value)));
    MockServerWebExchange exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

    // Capture the ServerWebExchange passed to the filter chain
    ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(ServerWebExchange.class);
    // Call the filter method with the mock exchange
    filter.filter(exchange, filterChain)
        .as(StepVerifier::create)
        .verifyComplete();

    // Verify that the filter chain was called with the sanitized exchange
    verify(filterChain).filter(exchangeCaptor.capture());
    ServerWebExchange sanitizedExchange = exchangeCaptor.getValue();
    Flux<DataBuffer> sanitizedBody = sanitizedExchange.getRequest().getBody();

    // Convert the sanitized body to a string for verification
    String result = sanitizedBody.map(dataBuffer -> {
      byte[] bytes = new byte[dataBuffer.readableByteCount()];
      dataBuffer.read(bytes);
      return new String(bytes, StandardCharsets.UTF_8);
    }).blockFirst();
    return result;
  }

  /**
   * Helper method to convert a string to a DataBuffer.
   *
   * @param str the string to convert
   * @return the DataBuffer representation of the string
   */
  private DataBuffer stringToDataBuffer(String str) {
    return new DefaultDataBufferFactory().wrap(str.getBytes(StandardCharsets.UTF_8));
  }
}
