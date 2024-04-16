package com.lisi.booknavigator.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll());  // 允许所有请求 Test only
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));  // use Default explicitly 6.1
            /*
                .pathMatchers("/eureka/**").permitAll() // allow Eureka call without condition
                .anyExchange().authenticated()) // all others should be authenticated
                 */
        return serverHttpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:8000")); // 允许的源
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 允许的方法
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // 允许的头部
        corsConfig.setAllowCredentials(true); // 允许证书

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 对所有路径应用此CORS配置
        return source;
    }

}
