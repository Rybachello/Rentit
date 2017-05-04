package com.example.sales.integration;

import com.example.sales.application.dto.RemittanceDTO;
import com.example.sales.application.services.InvoiceService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

@Configuration
public class InvoicingFlow {

    @Value("${gmail.username}")
    String gmailUsername;

    @Value("${gmail.password}")
    String gmailPassword;

    @Autowired
    InvoiceService invoiceService;

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

    @Bean
    IntegrationFlow inboundHttpGateway() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/remittances/remittance").requestPayloadType(String.class))
                .channel("remittance-channel")
                .get();
    }

    @Bean
    IntegrationFlow inboundMail() {
        return IntegrationFlows.from(Mail.imapInboundAdapter(
                String.format("imaps://%s:%s@imap.gmail.com:993/INBOX", gmailUsername, gmailPassword)
                )
                        .selectorExpression("subject matches '.*remittance.*'")
                , e -> e.autoStartup(true).poller(Pollers.fixedDelay(10000)))
                .transform("@RemittanceProcessor.extractRemittance(payload)")
                .channel("remittance-channel")
                .get();
    }

    @Service
    class RemittanceProcessor {
        public String extractRemittance(MimeMessage msg) throws Exception {
            Multipart multipart = (Multipart) msg.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.getContentType().contains("json") &&
                        bodyPart.getFileName().startsWith("remittance"))
                    return IOUtils.toString(bodyPart.getInputStream(), "UTF-8");
            }
            throw new Exception("oops at extractRemmittance");
        }
    }

    @Bean
    IntegrationFlow router() {
        return IntegrationFlows.from("remittance-channel")
                .handle(x -> invoiceService.closeInvoice((RemittanceDTO) x.getPayload()))
                .get();
    }

}

