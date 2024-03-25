package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A WebFilter implementation for sanitizing user input to prevent XSS attacks in a Spring Boot WebFlux application.
 * This filter intercepts incoming requests and sanitizes the request body by removing any potentially malicious
 * HTML content using the jsoup library.
 */
@Component
public class XssSanitizationFilter implements WebFilter {

  private static final Logger logger = LogManager.getLogger(XssSanitizationFilter.class);

  /**
   * Filters the incoming request to sanitize its body.
   *
   * @param exchange the current server exchange
   * @param chain    provides a way to delegate to the next filter
   * @return a {@link Mono} that indicates when request handling is complete
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest sanitizedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
      @Override
      public Flux<DataBuffer> getBody() {
        return super.getBody().map(dataBuffer -> {
          byte[] bytes = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(bytes);
          String bodyString = new String(bytes, StandardCharsets.UTF_8);
          logger.debug("### Original request body: {}", bodyString);
          // Sanitize the bodyString using Jsoup to remove any HTML tags, preventing XSS attacks.
          String sanitizedBody = Jsoup.clean(bodyString, Safelist.none());
          logger.debug("### Sanitized request body: {}", sanitizedBody);
          return exchange.getResponse().bufferFactory().wrap(sanitizedBody.getBytes(StandardCharsets.UTF_8));
        });
      }
    };
    return chain.filter(exchange.mutate().request(sanitizedRequest).build());
  }
}
