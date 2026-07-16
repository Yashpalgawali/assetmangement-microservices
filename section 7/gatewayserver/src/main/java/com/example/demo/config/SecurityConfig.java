package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	  @Bean
	  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
	        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
	        		.pathMatchers("/assetmanagement/company/**").authenticated()
	        		.pathMatchers("/assetmanagement/department/**").authenticated()
	        		.pathMatchers("/assetmanagement/designation/**").authenticated())
	        .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));
	        
	        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
	        return serverHttpSecurity.build();
	    }
}
