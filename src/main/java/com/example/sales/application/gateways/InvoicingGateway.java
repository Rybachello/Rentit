package com.example.sales.application.gateways;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import javax.mail.internet.MimeMessage;

/**
 * Created by stepan on 15/05/2017.
 */
@MessagingGateway
public interface InvoicingGateway {
    @Gateway(requestChannel="sendEmailChannel")
    void sendInvoice(MimeMessage msg);
    @Gateway(requestChannel="sendEmailChannel")
    void sendNotification(MimeMessage msg);
}
