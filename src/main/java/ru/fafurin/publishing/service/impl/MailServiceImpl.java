package ru.fafurin.publishing.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    /**
     * Отправить электронное письмо заказчику
     *
     * @param customer - заказчик
     * @param type     - тип письма
     * @param props    - объекты, которые будут переданы в письме
     */
    @Override
    public void sendEmail(Customer customer, MailType type, HashMap<String, Object> props) {
        switch (type) {
            case CREATION -> sendCreationEmail(customer);
            case REPORT -> sendReportEmail(customer, props);
            default -> {
            }
        }
    }

    /**
     * Отправить заказчику письмо при успешном оформлении заказа
     *
     * @param customer - заказчик
     */
    public void sendCreationEmail(Customer customer) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setSubject(customer.getName() + ", ваш заказ принят в работу. Спасибо за оказанное доверие!");
            helper.setTo(customer.getEmail());
            String emailContent = getCreationEmailContent(customer);
            helper.setText(emailContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    /**
     * Отправить заказчику отчет о проделанной работе
     *
     * @param customer - заказчик
     * @param props    - объекты, которые будут переданы в письме
     */
    public void sendReportEmail(Customer customer, HashMap<String, Object> props) {
        MimeMessage message = mailSender.createMimeMessage();
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
        mailSender.send(message);
    }

    /**
     * Сформировать фаблон письма для отправки зазазчику при успешном оформлении заказа
     *
     * @param customer - заказчик
     * @return содержимое письма
     */
    public String getCreationEmailContent(Customer customer) {
        Context context = new Context();
        StringWriter writer = new StringWriter();
        context.setVariable("customer", customer);
        templateEngine.process("mail/creation", context, writer);
        return writer.getBuffer().toString();
    }

    /**
     * Сформировать фаблон отчета для отправки зазазчику
     *
     * @param customer - заказчик
     * @param props    - объекты, которые будут переданы в письме
     * @return содержимое письма
     */
    public String getReportEmailContent(Customer customer, HashMap<String, Object> props) {
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
