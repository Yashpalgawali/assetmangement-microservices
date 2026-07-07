package com.example.demo;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

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
											  .addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString() )
											  .circuitBreaker(config -> config.setName("companyCircuitBreaker")
													  						.setFallbackUri("forward:/contactSupport")
													  )
											   
										)
								.uri("lb://COMPANY"))
						
						.route(p-> p.path("/assetmanagement/department/**")
								.filters(f -> 
											f.rewritePath("/assetmanagement/department/(?<segment>.*)", "/${segment}")
											.addResponseHeader("X-RESPONSE-TIME", LocalDateTime.now().toString())
											.circuitBreaker(config -> config.setName("departmentCircuitBreaker"))
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
}
