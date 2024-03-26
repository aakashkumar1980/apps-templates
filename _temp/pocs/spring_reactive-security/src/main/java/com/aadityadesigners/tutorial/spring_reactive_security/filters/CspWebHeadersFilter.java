package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Web filter to add Content Security Policy headers.
 * CSP headers are used to prevent Cross-Site Scripting (XSS) attacks by restricting the sources from which a web page can load resources.
 * - default-src 'self' restricts the sources from which resources can be loaded to the same origin.
 * - script-src 'self' 'unsafe-inline' restricts the sources from which scripts can be loaded to the same origin and inline scripts.
 * - style-src 'self' 'unsafe-inline' restricts the sources from which styles can be loaded to the same origin and inline styles.
 * - unsafe-inline allows the use of inline scripts and styles.
 *
 */
@Component
public class CspWebHeadersFilter implements WebFilter {

  /**
   * Filters incoming requests to add Content Security Policy headers to the response.
   *
   * @param exchange the current server exchange
   * @param chain    the web filter chain
   * @return a Mono<Void> that indicates when request handling is complete
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    exchange.getResponse().getHeaders().add(
        "Content-Security-Policy",
        "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';"
    );
    return chain.filter(exchange);
  }
}
