package com.tl.cuentas.config;

import org.h2.tools.Server;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.sql.SQLException;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AppConfig {

    @Value("${spring.h2.server.port}")
    private String h2Port;

    @Bean
    public AuditorAware auditorProvider() {
        return Optional::empty;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean localValidator = new LocalValidatorFactoryBean();
        localValidator.setValidationMessageSource(messageSource());

        return localValidator;
    }

    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server createH2TcpServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2Port);
    }
}
