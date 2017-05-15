package com.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.mail.Mail;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RentIt {

    @Value("${gmail.username}")
    String gmailUsername;

    @Value("${gmail.password}")
    String gmailPassword;

    @Configuration
    static class ObjectMapperCustomizer {
        @Autowired
        @Qualifier("_halObjectMapper")
        private ObjectMapper springHateoasObjectMapper;

        @Bean(name = "objectMapper")
        ObjectMapper objectMapper() {
            return springHateoasObjectMapper
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .registerModules(new JavaTimeModule());
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RentIt.class, args);
    }

    @Bean
    IntegrationFlow sendInvoiceFlow() {
        return IntegrationFlows.from("sendInvoiceChannel")
                .handle(Mail.outboundAdapter("smtp.gmail.com")
                        .port(465)
                        .protocol("smtps")
                        .credentials(gmailUsername, gmailPassword)
                        .javaMailProperties(p -> p.put("mail.debug", "false")))
                .get();
    }
}
