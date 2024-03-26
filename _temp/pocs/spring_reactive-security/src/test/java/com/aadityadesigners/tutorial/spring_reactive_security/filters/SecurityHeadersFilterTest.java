package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class SecurityHeadersFilterTest {

  private SecurityHeadersFilter filter;
  private WebFilterChain filterChain;

  @BeforeEach
  public void setUp() {
    filter = new SecurityHeadersFilter();
    filterChain = mock(WebFilterChain.class);
    when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
  }

  @Test
  public void testAddSecurityHeaders() {
    MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
    filter.filter(exchange, filterChain).block();

    HttpHeaders headers = exchange.getResponse().getHeaders();
    assertEquals("nosniff", headers.getFirst("X-Content-Type-Options"));
    assertEquals("DENY", headers.getFirst("X-Frame-Options"));
    assertEquals("1; mode=block", headers.getFirst("X-XSS-Protection"));
    assertEquals("default-src 'self'", headers.getFirst("Content-Security-Policy"));
  }
}
