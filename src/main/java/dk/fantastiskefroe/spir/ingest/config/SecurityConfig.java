package dk.fantastiskefroe.spir.ingest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .matchers(ServerWebExchangeMatchers.anyExchange()).permitAll()
                );

        return http.build();
    }
}
