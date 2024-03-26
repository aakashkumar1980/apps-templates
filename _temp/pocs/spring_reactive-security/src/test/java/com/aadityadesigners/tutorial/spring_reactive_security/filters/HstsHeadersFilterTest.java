package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Test class for HstsHeadersFilter.
 */
public class HstsHeadersFilterTest {

  private HstsHeadersFilter filter;
  private WebFilterChain filterChain;

  @BeforeEach
  public void setUp() {
    filter = new HstsHeadersFilter();
    filterChain = mock(WebFilterChain.class);
    when(filterChain.filter(any(MockServerWebExchange.class))).thenReturn(Mono.empty());
  }

  @Test
  public void testAddSecurityHeaders() {
    MockServerHttpRequest request = MockServerHttpRequest.get("https://example.com").build();
    MockServerWebExchange exchange = MockServerWebExchange.from(request);
    filter.filter(exchange, filterChain).block();

    String hstsHeader = exchange.getResponse().getHeaders().getFirst("Strict-Transport-Security");
    assert hstsHeader != null;
    assert hstsHeader.contains("max-age=31536000; includeSubDomains");
  }

}

