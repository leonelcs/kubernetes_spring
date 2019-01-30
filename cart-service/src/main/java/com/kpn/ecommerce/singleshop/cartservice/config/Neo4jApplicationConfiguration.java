package com.kpn.ecommerce.singleshop.cartservice.config;

import com.kpn.ecommerce.singleshop.cartservice.cart.domain.Cart;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableNeo4jRepositories(basePackages = "com.kpn.ecommerce.singleshop.cartservice")
public class Neo4jApplicationConfiguration {

    @NonNull private final Neo4jApplicationParameterConfiguration applicationConfiguration;

    @Bean public org.neo4j.ogm.config.Configuration configuration() {
        return new org.neo4j.ogm.config.Configuration.Builder().uri(applicationConfiguration.getUri())
            .credentials(applicationConfiguration.getUsername(), applicationConfiguration.getPassword()).build();
    }

    @Bean public SessionFactory sessionFactory() {
        return new SessionFactory(configuration(), getModelPackageNames(Cart.class));
    }


    @Bean public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

    private String[] getModelPackageNames(final Class<?>... modelClasses) {
        return Arrays.stream(modelClasses).map(Class::getPackage).map(Package::getName).toArray(String[]::new);
    }
}
