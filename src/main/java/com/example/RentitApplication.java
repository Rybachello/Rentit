package com.example;

import com.example.sales.domain.model.Remittance;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

@EnableIntegration
@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RentitApplication {

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

//    @Bean
//    IntegrationFlow inboundHttpGateway() {
//        return IntegrationFlows.from(
//                Http.inboundChannelAdapter("/api/remittances/remittance").requestPayloadType(String.class))
//                .channel("router-channel")
//                .get();
//    }
//
//    @Service
//    class RemittanceProcessor {
//        public String extractRemmittance(MimeMessage msg) throws Exception {
//            Multipart multipart = (Multipart) msg.getContent();
//            for (int i = 0; i < multipart.getCount(); i++) {
//                BodyPart bodyPart = multipart.getBodyPart(i);
//                if (bodyPart.getContentType().contains("json") &&
//                        bodyPart.getFileName().startsWith("remittance"))
//                    return IOUtils.toString(bodyPart.getInputStream(), "UTF-8");
//            }
//            throw new Exception("oops at extractRemmittance");
//        }
//    }
//
//    @Value("${gmail.username}")
//    String gmailUsername;
//
//    @Value("${gmail.password}")
//    String gmailPassword;
//
//    @Bean
//    IntegrationFlow inboundMail() {
//        return IntegrationFlows.from(Mail.imapInboundAdapter(
//                String.format("imaps://%s:%s@imap.gmail.com:993/INBOX", gmailUsername, gmailPassword)
//                )
//                        .selectorExpression("subject matches '.*remittance.*'")
//                , e -> e.autoStartup(true).poller(Pollers.fixedDelay(10000)))
//                .transform("@RemittanceProcessor.extractRemittance(payload)")
//                .channel("remittance-channel")
//                .get();
//    }
//
//    @Bean
//    IntegrationFlow router() {
//        return IntegrationFlows.from("remittance-channel")
//                .handle(x -> RemittanceHandle((Remittance) x.getPayload()))
//                .get();
//    }
//
//    void RemittanceHandle(Remittance remittance) {
//
//    }
//
//    @Bean
//    IntegrationFlow normalTrack() {
//        return IntegrationFlows.from("normaltrack-channel")
//                .handle(i -> System.out.println(i))
//                .get();
//    }
//
//    @Bean
//    IntegrationFlow fastTrack() {
//        return IntegrationFlows.from("fasttrack-channel")
//                .handle(System.err::println)
//                .get();
//    }
//

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RentitApplication.class, args);
    }
}
