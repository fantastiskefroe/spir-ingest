package dk.fantastiskefroe.spir.ingest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private final Mac mac;

    public SecurityConfig(ApplicationProperties applicationProperties) {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(applicationProperties.getHmacPrivateKey().getBytes(), "HmacSHA256");
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    private final String hmacKey;

    public SecurityConfig(ApplicationProperties applicationProperties) {
        this.hmacKey = applicationProperties.getHmacPrivateKey();
    }


    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:8080", "https://spir.fantastiskefroe.dk"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()

                .addFilterAt((exchange, chain) ->
                        chain.filter(new CachingServerWebExchangeDecorator(exchange))
                        , SecurityWebFiltersOrder.FIRST)
                .authorizeExchange()
                .pathMatchers("/webhook/**")
                .access((authentication, context) -> {
                    final String hmac = context.getExchange().getRequest().getHeaders().getFirst("X-Shopify-Hmac-Sha256");
                    return context.getExchange().getRequest()
                            .getBody()
                            .map(SecurityConfig::dataBufferToString)
                            .collectList()
                            .map(l -> String.join("", l))
                            .map(body -> new AuthorizationDecision(validateWebhook(body, hmac)));
                })
                .anyExchange().permitAll()
                .and().build();
    }

    private static String dataBufferToString(DataBuffer dataBuffer) {
        final byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private boolean validateWebhook(String data, String hmac) {
        return calculateHmac(data).equals(hmac);
    }

    private String calculateHmac(String data) {
        return ENCODER.encodeToString(mac.doFinal(data.getBytes()));
    }
}
