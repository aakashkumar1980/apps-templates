package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filter to add HSTS headers to the response.
 * - This header tells the browser to always use HTTPS for the domain and all subdomains.
 * - The max-age value is the number of seconds that the browser should remember to use HTTPS.
 * - The includeSubDomains value tells the browser to use HTTPS for all subdomains.
 *
 */
@Component
public class HstsHeadersFilter implements WebFilter {

  /**
   * Add HSTS headers to the response.
   *
   * @param exchange the server web exchange
   * @param chain the web filter chain
   * @return a mono of void
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();

    if (request.getURI().getScheme()!=null && request.getURI().getScheme().equals("https")) {
      response.getHeaders().add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    }

    return chain.filter(exchange);
  }
}

