package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * A WebFilter that adds security headers to the response.
 * - X-Content-Type-Options: Prevents browsers from MIME-sniffing a response away from the declared content-type.
 * - X-Frame-Options: Provides clickjacking protection.
 * - X-XSS-Protection: Enables the Cross-site scripting (XSS) filter built into most browsers.
 * - Content-Security-Policy: Prevents a wide range of attacks, including Cross-site scripting and other code injection attacks.
 */
@Component
public class SecurityHeadersFilter implements WebFilter {

  /**
   * Filters incoming requests to add security headers to the response.
   *
   * @param exchange the current server exchange
   * @param chain    the web filter chain
   * @return a Mono<Void> that indicates when request handling is complete
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    exchange.getResponse().getHeaders().add("X-Content-Type-Options", "nosniff");
    exchange.getResponse().getHeaders().add("X-Frame-Options", "DENY");
    exchange.getResponse().getHeaders().add("X-XSS-Protection", "1; mode=block");
    exchange.getResponse().getHeaders().add("Content-Security-Policy", "default-src 'self'");
    return chain.filter(exchange);
  }
}

