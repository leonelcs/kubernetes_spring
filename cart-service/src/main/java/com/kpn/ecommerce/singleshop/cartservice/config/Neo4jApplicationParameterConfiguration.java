package com.kpn.ecommerce.singleshop.cartservice.config;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="spring.data.neo4j")
@Data
public class Neo4jApplicationParameterConfiguration {

	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	@NotNull
	private String uri;
}
