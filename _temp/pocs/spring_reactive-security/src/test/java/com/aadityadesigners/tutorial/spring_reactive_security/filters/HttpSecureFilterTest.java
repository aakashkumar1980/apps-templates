package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Test class for {@link HttpSecureFilter}.
 */
public class HttpSecureFilterTest {

  private HttpSecureFilter filter;
  private WebFilterChain filterChain;

  @BeforeEach
  public void setUp() {
    filter = new HttpSecureFilter();
    filterChain = mock(WebFilterChain.class);
    when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
  }

  @Test
  public void testAddSecurityHeaders() {
    MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
    exchange.getResponse().addCookie(ResponseCookie.from("testCookie", "testValue").build());
    filter.filter(exchange, filterChain).block();

    exchange.getResponse().getCookies().forEach((name, cookies) -> cookies.forEach(cookie -> {
      assert cookie.isHttpOnly();
      assert cookie.isSecure();
    }));
  }
}

