package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * A WebFilter that adds the Secure and HttpOnly flags to all cookies in the response.
 * - Secure flag ensures that the cookie is only sent over HTTPS connections.
 * - HttpOnly flag ensures that the cookie is not accessible via JavaScript.
 */
@Component
public class HttpSecureFilter implements WebFilter {

  /**
   * Filters the response to add the Secure and HttpOnly flags to all cookies.
   *
   * @param exchange the current server exchange
   * @param chain    the web filter chain
   * @return a Mono<Void> that indicates when request handling is complete
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return chain.filter(exchange)
        .then(Mono.fromRunnable(() -> {
          // Collect existing cookie names
          List<ResponseCookie> existingCookies = exchange.getResponse().getCookies().values().stream()
              .flatMap(List::stream)
              .collect(Collectors.toList());

          // Remove all existing cookies
          if(!exchange.getResponse().getCookies().isEmpty())
            exchange.getResponse().getCookies().clear();

          // Add updated cookies
          existingCookies.forEach(cookie -> {
            ResponseCookie updatedCookie = ResponseCookie.from(cookie.getName(), cookie.getValue())
                .httpOnly(true)
                .secure(true)
                .path(cookie.getPath())
                .domain(cookie.getDomain())
                .maxAge(cookie.getMaxAge())
                .build();
            exchange.getResponse().addCookie(updatedCookie);
          });
        }));
  }
}
