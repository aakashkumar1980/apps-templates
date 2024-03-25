package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Test class for {@link SecurityHeadersFilter}.
 */
@WebFluxTest
@Import(SecurityHeadersFilter.class)
public class SecurityHeadersFilterTest {

  @Autowired
  private WebTestClient webTestClient;

  /**
   * Test that security headers are present in the response.
   */
  @Test
  public void securityHeadersShouldBePresentInResponse() {
    webTestClient.get().uri("/")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().exists("X-Content-Type-Options")
        .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
        .expectHeader().exists("X-Frame-Options")
        .expectHeader().valueEquals("X-Frame-Options", "DENY")
        .expectHeader().exists("X-XSS-Protection")
        .expectHeader().valueEquals("X-XSS-Protection", "1; mode=block")
        .expectHeader().exists("Content-Security-Policy")
        .expectHeader().valueEquals("Content-Security-Policy", "default-src 'self'");
  }
}
