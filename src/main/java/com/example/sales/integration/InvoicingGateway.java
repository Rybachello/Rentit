package com.example.sales.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import javax.mail.internet.MimeMessage;

@MessagingGateway
public interface InvoicingGateway {
    @Gateway(requestChannel = "sendInvoice-http-channel")
    public void sendInvoice(MimeMessage message);
}