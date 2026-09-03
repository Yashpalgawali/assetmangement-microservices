package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
		serverHttpSecurity
				.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
						.pathMatchers("/assetmanagement/company/**").hasAnyRole("USER", "ADMIN")
						.pathMatchers("/assetmanagement/department/**").hasAnyRole("USER", "ADMIN")
						.pathMatchers("/assetmanagement/designation/**").hasAnyRole("USER", "ADMIN")
						.pathMatchers("/assetmanagement/asset/**").hasAnyRole("USER", "ADMIN"))
				.oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
						.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));

		serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
		return serverHttpSecurity.build();
	}

	// 3. Define Reactive CORS Configuration Source
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow both LAN IP and localhost frontend origins
        configuration.setAllowedOrigins(Arrays.asList(
                "http://192.168.0.219:3000",
                "http://localhost:3000"
        ));
        
        // Or if you want to allow any origin in dev:
        // configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "x-requested-with", "*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        // Note: Make sure to import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}
}
