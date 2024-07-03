package ru.fafurin.publishing.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;
import ru.fafurin.publishing.service.MailService;

import java.io.StringWriter;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(Customer customer, MailType type, HashMap<String, Object> props) {
        switch (type) {
            case CREATION -> sendRegistrationEmail(customer);
            case REPORT -> sendReportEmail(customer, props);
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(Customer customer) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setSubject(customer.getName() + ", ваш заказ принят в работу. Спасибо за оказанное доверие!");
        helper.setTo(customer.getEmail());
        String emailContent = getRegistrationEmailContent(customer);
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    private void sendReportEmail(Customer customer, HashMap<String, Object> props) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject(customer.getName() + ", в письме - отчет по вашему заказу.");
        helper.setTo(customer.getEmail());
        String emailContent = getReportEmailContent(customer, props);
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(Customer customer) {
        Context context = new Context();
        StringWriter writer = new StringWriter();
        context.setVariable("customer", customer);
        templateEngine.process("mail/registration", context, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getReportEmailContent(Customer customer, HashMap<String, Object> props) {
        Context context = new Context();
        StringWriter writer = new StringWriter();
        context.setVariable("customer", customer);
        context.setVariable("order", props.get("order"));
        context.setVariable("book", props.get("book"));
        context.setVariable("tasks", props.get("tasks"));
        templateEngine.process("mail/report", context, writer);
        return writer.getBuffer().toString();
    }

}
