package com.example.demo;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);		
	}

	@Bean
	RouteLocator assetManagementRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		
		return routeLocatorBuilder.routes()
						.route(p-> p.path("/assetmanagement/company/**")
								.filters(f -> 
											f.rewritePath("/assetmanagement/company/(?<segment>.*)", "/${segment}")
											  .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
											  .retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET).setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2, true))
										)
								.uri("lb://COMPANY"))
						
						.route(p-> p.path("/assetmanagement/department/**")
								.filters(f -> 
											f.rewritePath("/assetmanagement/department/(?<segment>.*)", "/${segment}")
											.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
											.circuitBreaker(config -> config.setName("departmentCircuitBreaker").setFallbackUri("forward:/contactSupport"))
											.requestRateLimiter(config-> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver()) )
										)
								.uri("lb://DEPARTMENT"))
						
						.route(p-> p.path("/assetmanagement/designation/**")
								.filters(f -> 
											f.rewritePath("/assetmanagement/designation/(?<segment>.*)", "/${segment}")
											.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString() )
										)
								.uri("lb://DESIGNATION"))
						.build();
	}
	
	@Bean
	Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id ->new Resilience4JConfigBuilder(id)
				  .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				  .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build()
				);
	}
	@Bean
	RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1,1,1);
	}
	
	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user")).defaultIfEmpty("anonymous");
	}
	
}
