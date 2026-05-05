package com.pedrocmoreira.garagesystem.infrastructure.mail;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.presentation.dto.EmailItemDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String mailFrom;

    @Value("${app.base-url}")
    private String baseUrl;


    public void sendBudget(ServiceOrder serviceOrder) {
        String customerEmail = serviceOrder.getCustomer().getEmail();

        log.info("=== INICIANDO ENVIO DE EMAIL para OS {} ===", serviceOrder.getNumber());
        log.info("Email do cliente: {}", customerEmail);


        if(customerEmail == null || customerEmail.isBlank()) {
            log.warn("O cliente {} não possui um e-mail cadastrado. O orçamento não foi enviado.", serviceOrder.getCustomer().getName());
            return;
        }

        try {
            Context context = new Context(Locale.forLanguageTag("pt-BR"));
            context.setVariable("customerName", serviceOrder.getCustomer().getName());
            context.setVariable("orderServiceNumber", serviceOrder.getNumber());
            context.setVariable("vehiclePlate", serviceOrder.getVehicle().getPlate());
            context.setVariable("vehicleMake", serviceOrder.getVehicle().getMake());
            context.setVariable("vehicleModel",serviceOrder.getVehicle().getMake());
            context.setVariable("vehicleYear",   serviceOrder.getVehicle().getYear());
            context.setVariable("totalValue",   serviceOrder.getTotalValue());
            context.setVariable("observations",  serviceOrder.getObservations());
            context.setVariable("approveUrl", baseUrl + "/api/budget/" + serviceOrder.getNumber() + "/approve");
            context.setVariable("refuseUrl",  baseUrl + "/api/budget/" + serviceOrder.getNumber() + "/refuse");

            List<EmailItemDTO> services = serviceOrder.getServiceItems().stream()
                    .map(i -> new EmailItemDTO(
                            i.getService().getName(),
                            null,
                            i.getAppliedPrice()))
                    .toList();

            List<EmailItemDTO> parts = serviceOrder.getPartItems().stream()
                    .map(i -> new EmailItemDTO(
                            i.getPart().getName(),
                            i.getQuantity(),
                            i.getSubtotal()))
                    .toList();

            context.setVariable("services", services);
            context.setVariable("parts", parts);

            String html = templateEngine.process("email/budget", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(customerEmail);
            helper.setSubject("Orçamento " + serviceOrder.getNumber() + " — Garage System");
            helper.setText(html, true);
            log.info("Tentando enviar email para {} via {}:{}", customerEmail, mailFrom, baseUrl);
            mailSender.send(message);
            log.info("=== EMAIL ENVIADO COM SUCESSO para {} ===", customerEmail);

        } catch (Exception e) {  // ← era MessagingException
            log.error("=== ERRO AO ENVIAR EMAIL ===: {}", e.getMessage(), e);
        }
    }
}
