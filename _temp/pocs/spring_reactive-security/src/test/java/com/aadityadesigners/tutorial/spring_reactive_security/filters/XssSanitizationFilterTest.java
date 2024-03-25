package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Test class for {@link XssSanitizationFilter}.
 */
public class XssSanitizationFilterTest {

  private XssSanitizationFilter filter;
  @Mock
  private WebFilterChain filterChain;

  @Captor
  private ArgumentCaptor<ServerWebExchange> exchangeCaptor;

  /**
   * Set up the test environment before each test.
   */
  @BeforeEach
  public void setUp() {
    // initialize all mock objects at once without having to call Mockito.mock(Class<T> classToMock) individually for each field.
    MockitoAnnotations.openMocks(this);

    filter = new XssSanitizationFilter();
    when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
  }

  /**
   * Test that the XssSanitizationFilter sanitizes the request body.
   */
  @Test
  public void testSanitizeRequestBody() {
    String maliciousContent = "<script>alert('XSS');</script>";
    String sanitizedContent = "";

    StringBuilder bodyContent = getFilteredContent(maliciousContent);
    assertEquals(sanitizedContent, bodyContent.toString());
  }

  /**
   * Test that the XssSanitizationFilter does not modify a standard request body.
   */
  @Test
  public void testStandardRequestBody() {
    String maliciousContent = "Hello, World!";
    String sanitizedContent = "Hello, World!";

    StringBuilder bodyContent = getFilteredContent(maliciousContent);
    assertEquals(sanitizedContent, bodyContent.toString());
  }

  /**
   * Helper method to test the filter method of the XssSanitizationFilter.
   *
   * @param content the content to filter
   * @return the filtered content
   */
  private StringBuilder getFilteredContent(String content) {
    ServerHttpRequest request = MockServerHttpRequest
        .post("/")
        .body(content);
    MockServerWebExchange exchange = MockServerWebExchange.builder((MockServerHttpRequest) request).build();

    // Create a StepVerifier to test the reactive behavior of the filter method, and
    // Verify that the Mono<Void> returned by the filter method completes successfully without emitting any value.
    filter.filter(exchange, filterChain)
        .as(StepVerifier::create)
        .verifyComplete();

    // Verify that the filter method was called on the filterChain with the expected ServerWebExchange.
    Mockito.verify(filterChain).filter(exchangeCaptor.capture());
    // Capture the ServerWebExchange argument passed to the filter method.
    ServerWebExchange capturedExchange = exchangeCaptor.getValue();
    // Extract the body of the captured ServerWebExchange as a Flux<DataBuffer>.
    Flux<DataBuffer> body = capturedExchange.getRequest().getBody();
    // Convert the Flux<DataBuffer> to a String for further assertions.
    StringBuilder bodyContent = new StringBuilder();
    body.collectList().block().forEach(buffer -> {
      // Read the contents of each DataBuffer into a byte array.
      byte[] bytes = new byte[buffer.readableByteCount()];
      buffer.read(bytes);
      // Append the string representation of the byte array to the StringBuilder.
      bodyContent.append(new String(bytes, StandardCharsets.UTF_8));
    });
    return bodyContent;
  }
}
