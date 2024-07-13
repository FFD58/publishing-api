package ru.fafurin.publishing.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;

import java.io.StringWriter;
import java.util.HashMap;

/**
 * Сервис для генерации отчета по заказу
 */
@Component
@RequiredArgsConstructor
public class CreateReportMailGenerator implements MailGenerator {

    private final TemplateEngine templateEngine;

    private static final MailType MAIL_TYPE = MailType.ORDER_REPORT;

    /**
     * Сгенерировать отчет о проделанной работе
     *
     * @param customer - заказчик
     * @param props    - объекты, которые будут переданы в письме
     */
    @Override
    public MimeMessage generate(MimeMessage message, Customer customer, HashMap<String, Object> props) {
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setSubject(customer.getName() + ", в письме - отчет по вашему заказу.");
            helper.setTo(customer.getEmail());
            String emailContent = getReportEmailContent(customer, props);
            helper.setText(emailContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    /**
     * Создать шаблон письма
     *
     * @param customer - заказчик
     * @return шаблон письма
     */
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

    @Override
    public MailType getMyType() {
        return MAIL_TYPE;
    }
}
