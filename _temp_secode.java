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

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InputSanitizationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI originalUri = request.getURI();

        String sanitizedQuery = originalUri.getQuery() != null
                ? sanitizeQueryParameters(originalUri.getQuery())
                : "";

        URI sanitizedUri = URI.create(originalUri.getScheme() + "://" +
                originalUri.getHost() +
                (originalUri.getPort() != -1 ? ":" + originalUri.getPort() : "") +
                originalUri.getPath() +
                (sanitizedQuery.isEmpty() ? "" : "?" + sanitizedQuery));

        ServerHttpRequest sanitizedRequest = request.mutate().uri(sanitizedUri).build();

        return chain.filter(exchange.mutate().request(sanitizedRequest).build());
    }

    private String sanitizeQueryParameters(String query) {
        return Map.of(query.split("&")).entrySet().stream()
                .map(entry -> entry.getKey() + "=" + InputSanitizer.sanitize(entry.getValue()))
                .collect(Collectors.joining("&"));
    }
}
This filter is specifically designed to sanitize user input in the request, such as query parameters, form data, or any other input that comes from the client. It uses a utility class (InputSanitizer) to escape special HTML characters, which helps prevent XSS attacks by ensuring that any input that might be included in the HTML output is safe.
It does not deal with adding security headers to the response.



/** ********** **/
/** VALIDATION **/
/** ********** **/
-- sample.json --
{
  "id": 123,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "isActive": true,
  "balance": 1000.50,
  "tags": ["premium", "new"],
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zipCode": "12345"
  },
  "orderHistory": [
    {
      "orderId": "ORD001",
      "date": "2022-01-10",
      "total": 150.75,
      "items": [
        {
          "itemId": "ITEM001",
          "name": "Widget A",
          "quantity": 2,
          "price": 25.00
        },
        {
          "itemId": "ITEM002",
          "name": "Widget B",
          "quantity": 1,
          "price": 100.75
        }
      ]
    }
  ],
  "socialSecurityNumber": {
    "tokenValue": "xxxxx"
  },
  "creditCard": {
    "creditCardNumber": {
      "tokenValue": "xxxxx" 
    },
    "encryptedPIN": {
      "encryptedValue": "xxxxx"  
    }
  }
}


-- root.yml --
# root.yml
definitions:
  tokenValue:
    type: string
  encryptedValue:
    type: string

-- main.yml --
$ref: 'root.yml#/definitions'

type: object
properties:
  id:
    type: integer
  name:
    type: string
  email:
    type: string
  isActive:
    type: boolean
  balance:
    type: number
  tags:
    type: array
    items:
      type: string
  address:
    type: object
    properties:
      street:
        type: string
      city:
        type: string
      state:
        type: string
      zipCode:
        type: string
  orderHistory:
    type: array
    items:
      type: object
      properties:
        orderId:
          type: string
        date:
          type: string
        total:
          type: number
        items:
          type: array
          items:
            type: object
            properties:
              itemId:
                type: string
              name:
                type: string
              quantity:
                type: integer
              price:
                type: number
  socialSecurityNumber:
    type: object
    properties:
      tokenValue:
        $ref: 'root.yml#/definitions/tokenValue'
  creditCard:
    type: object
    properties:
      creditCardNumber:
        type: object
        properties:
          tokenValue:
            $ref: 'root.yml#/definitions/tokenValue'
      encryptedPIN:
        type: object
        properties:
          encryptedValue:
            $ref: 'root.yml#/definitions/encryptedValue'
required:
  - id
  - name
  - email
  - isActive
  - balance
  - tags
  - address
  - orderHistory
  - socialSecurityNumber
  - creditCard



import java.util.Set;
public class ValidationException extends RuntimeException {
    private final Set<String> errors;

    public ValidationException(Set<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Set<String> getErrors() {
        return errors;
    }
}


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class JsonSchemaValidatorUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

    public static Set<String> validateJsonAgainstSchema(String json, String schemaFilePath) {
        Set<String> validationErrors = new HashSet<>();

        try {
            // Load JSON node
            JsonNode jsonNode = objectMapper.readTree(json);

            // Load YAML schema and convert to JSON schema
            Yaml yaml = new Yaml();
            try (InputStream inputStream = new FileInputStream(new File(schemaFilePath))) {
                Map<String, Object> yamlMap = yaml.load(inputStream);
                JsonNode schemaNode = objectMapper.valueToTree(yamlMap);
                JsonSchema schema = schemaFactory.getSchema(schemaNode);

                // Validate JSON against schema
                Set<ValidationMessage> errors = schema.validate(jsonNode);

                // Collect validation errors
                for (ValidationMessage error : errors) {
                    validationErrors.add(error.getMessage());
                }
            }
        } catch (Exception e) {
            validationErrors.add("Error while validating JSON against schema: " + e.getMessage());
        }

        return validationErrors;
    }
}



import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Set;
public class ValidationHandler {

    public static HandlerFunction<ServerResponse> validate(String schemaName, HandlerFunction<ServerResponse> next) {
        return request -> {
            ServerHttpRequest httpRequest = request.exchange().getRequest();
            return httpRequest.getBody().next().flatMap(buffer -> {
                String json = buffer.toString();

                Set<String> errors = JsonSchemaValidatorUtil.validateJsonAgainstSchema(
                    json,
                    "src/main/resources/validation/" + schemaName + ".yml"
                );

                if (!errors.isEmpty()) {
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse("Validation Error", errors));
                }

                return next.handle(request);
            });
        };
    }
}


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .POST("/sample", ValidationHandler.validate("sampleSchema", this::sampleHandler))
            .build();
    }

    private Mono<ServerResponse> sampleHandler(ServerRequest request) {
        // Your handler logic
        return ServerResponse.ok().bodyValue("Success");
    }
}



import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(ValidationException ex) {
        return new ErrorResponse("Validation Error", ex.getErrors());
    }
}


=== JUNIT ===
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

class SampleHandlerTest {

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<ServerResponse> routerFunction = route(POST("/sample"),
                ValidationHandler.validate("sampleSchema", request -> ServerResponse.ok().bodyValue("Success")));

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testSampleHandlerSuccess() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/sample_success.json")));

        webTestClient.post().uri("/sample")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Success");
    }

    @Test
    void testSampleHandlerFailure() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/sample_failure.json")));

        webTestClient.post().uri("/sample")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$[0]").isEqualTo("Validation error message");
    }
}
