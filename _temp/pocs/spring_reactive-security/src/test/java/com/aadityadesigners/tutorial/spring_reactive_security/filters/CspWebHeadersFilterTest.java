package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Test class for {@link CspWebHeadersFilter}.
 */
public class CspWebHeadersFilterTest {

  private CspWebHeadersFilter filter;
  private WebFilterChain filterChain;

  @BeforeEach
  public void setUp() {
    filter = new CspWebHeadersFilter();
    filterChain = mock(WebFilterChain.class);
    when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
  }

  @Test
  public void testCspWebHeaders() {
    MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
    filter.filter(exchange, filterChain).block();

    String cspHeader = exchange.getResponse().getHeaders().getFirst("Content-Security-Policy");
    assert cspHeader != null;
    assert cspHeader.contains("default-src 'self'");
    assert cspHeader.contains("script-src 'self' 'unsafe-inline'");
    assert cspHeader.contains("style-src 'self' 'unsafe-inline'");
  }
}
