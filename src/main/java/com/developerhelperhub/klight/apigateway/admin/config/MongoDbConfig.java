package com.developerhelperhub.klight.apigateway.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.Optional;


@Configuration
@EnableMongoAuditing
public class MongoDbConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator().getValidator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
            }
        };
    }

}
