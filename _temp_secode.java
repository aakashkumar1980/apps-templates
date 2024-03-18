import org.apache.commons.text.StringEscapeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class SecurityWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // Sanitize request parameters
        ServerHttpRequest sanitizedRequest = request.mutate()
                .headers(headers -> headers.forEach((key, values) -> {
                    values.replaceAll(value -> StringEscapeUtils.escapeHtml4(value));
                }))
                .build();

        // Add security headers to the response
        response.getHeaders().add(HttpHeaders.SET_COOKIE, "HttpOnly;Secure;SameSite=Strict");

        // Continue the filter chain with the sanitized request
        return chain.filter(exchange.mutate().request(sanitizedRequest).build());
    }
}
This filter focuses on adding security-related headers to the HTTP response, such as HttpOnly and Secure flags for cookies, which are important for preventing attacks like Cross-Site Scripting (XSS) and session hijacking.
It can also be used to sanitize request headers, but in the example provided, it mainly adds security headers to the response.


plugins {
    id 'org.springframework.boot' version '2.7.7'
    id 'io.spring.dependency-management' version '1.0.13.RELEASE'
    id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.apache.commons:commons-text:1.10.0'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'io.projectreactor:reactor-test'
}

test {
    useJUnitPlatform()
}







import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class TrustedSourceWebFilter implements WebFilter {

    private static final String TRUSTED_SOURCE_HEADER = "X-Trusted-Source";
    private static final String TRUSTED_SOURCE_VALUE = "my-trusted-source";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String headerValue = exchange.getRequest().getHeaders().getFirst(TRUSTED_SOURCE_HEADER);
        if (TRUSTED_SOURCE_VALUE.equals(headerValue)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
    }
}




import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InputSanitizationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest sanitizedRequest = exchange.getRequest().mutate()
                .queryParams(queryParams -> {
                    Map<String, String[]> sanitizedParams = queryParams.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> entry.getValue().stream()
                                            .map(InputSanitizer::sanitize)
                                            .toArray(String[]::new)
                            ));
                    return new QueryParamsAdapter(sanitizedParams);
                })
                .build();

        return chain.filter(exchange.mutate().request(sanitizedRequest).build());
    }

    private static class QueryParamsAdapter implements ServerHttpRequest.QueryParams {
        private final Map<String, String[]> queryParams;

        private QueryParamsAdapter(Map<String, String[]> queryParams) {
            this.queryParams = queryParams;
        }

        @Override
        public List<String> get(String key) {
            return Arrays.asList(queryParams.getOrDefault(key, new String[0]));
        }

        @Override
        public Set<String> keySet() {
            return queryParams.keySet();
        }

        @Override
        public MultiValueMap<String, String> toSingleValueMap() {
            LinkedMultiValueMap<String, String> singleValueMap = new LinkedMultiValueMap<>();
            queryParams.forEach((key, values) -> singleValueMap.add(key, values[0]));
            return singleValueMap;
        }
    }
}
This filter is specifically designed to sanitize user input in the request, such as query parameters, form data, or any other input that comes from the client. It uses a utility class (InputSanitizer) to escape special HTML characters, which helps prevent XSS attacks by ensuring that any input that might be included in the HTML output is safe.
It does not deal with adding security headers to the response.

