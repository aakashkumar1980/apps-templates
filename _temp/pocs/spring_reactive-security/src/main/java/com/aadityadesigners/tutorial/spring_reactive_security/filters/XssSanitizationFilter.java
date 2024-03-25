package com.aadityadesigners.tutorial.spring_reactive_security.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * A WebFilter that sanitizes JSON input to prevent XSS attacks and ensure valid JSON format.
 */
@Component
public class XssSanitizationFilter implements WebFilter {

  private static final Logger logger = LogManager.getLogger(XssSanitizationFilter.class);
  private static final Pattern UNWANTED_CHARS_PATTERN = Pattern.compile("[<>{}\"'`;]");
  private static final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper for JSON parsing

  /**
   * Filters incoming requests to sanitize JSON input.
   *
   * @param exchange the current server exchange
   * @param chain    the web filter chain
   * @return a Mono<Void> that indicates when request handling is complete
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    // Check if the request content type is application/json
    if (request.getHeaders().getContentType() != null &&
        request.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
      ServerHttpRequest sanitizedRequest = new ServerHttpRequestDecorator(request) {
        @Override
        public Flux<DataBuffer> getBody() {
          return super.getBody().map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            String bodyString = new String(bytes, StandardCharsets.UTF_8);
            logger.debug("### Original request body: {}", bodyString);

            try {
              // Parse and sanitize the JSON body
              JsonNode jsonNode = objectMapper.readTree(bodyString);
              sanitizeJsonNode(jsonNode);
              String sanitizedBody = objectMapper.writeValueAsString(jsonNode);
              logger.debug("### Sanitized request body: {}", sanitizedBody);
              return exchange.getResponse().bufferFactory().wrap(sanitizedBody.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
              // Log the error and reject the request if it's not valid JSON
              logger.error("Invalid JSON input", e);
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON input");
            }
          });
        }
      };
      return chain.filter(exchange.mutate().request(sanitizedRequest).build());

    } else {
      logger.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JSON datatype is allowed");
    }
  }

  /**
   * Recursively sanitizes a JsonNode by removing unwanted characters from textual nodes.
   *
   * @param node the JsonNode to sanitize
   */
  private void sanitizeJsonNode(JsonNode node) {
    if (node.isObject()) {
      ObjectNode objectNode = (ObjectNode) node;
      Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        JsonNode value = field.getValue();
        if (value.isTextual()) {
          // Sanitize text nodes by removing HTML and unwanted characters
          String sanitizedBody = Jsoup.clean(value.asText(), Safelist.none());
          sanitizedBody = UNWANTED_CHARS_PATTERN.matcher(sanitizedBody).replaceAll("");
          objectNode.put(field.getKey(), sanitizedBody);
        } else {
          // Recursively sanitize nested nodes
          sanitizeJsonNode(value);
        }
      }
    } else if (node.isArray()) {
      // Recursively sanitize array elements
      for (JsonNode element : node) {
        sanitizeJsonNode(element);
      }
    }
  }
}
