package ru.fafurin.publishing.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;

import java.io.StringWriter;
import java.util.HashMap;

/**
 * Сервис для генерации письма при создании заказа
 */
@Component
@RequiredArgsConstructor
public class CreateOrderMailGenerator implements MailGenerator {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final MailType MAIL_TYPE = MailType.ORDER_CREATE;

    /**
     * Сгенерировать письмо для отправки заказчику при создании заказа
     *
     * @param message  - сообщение электронной почты в стиле MIME
     * @param customer - заказчик
     * @param props    - дополнительные данные
     * @return сгенерированное письмо
     */
    @Override
    public MimeMessage generate(MimeMessage message, Customer customer, HashMap<String, Object> props) {
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
        return message;
    }

    /**
     * Создать шаблон письма
     *
     * @param customer - заказчик
     * @return шаблон письма
     */
    private String getCreationEmailContent(Customer customer) {
        Context context = new Context();
        StringWriter writer = new StringWriter();
        context.setVariable("customer", customer);
        templateEngine.process("mail/creation", context, writer);
        return writer.getBuffer().toString();
    }

    @Override
    public MailType getMyType() {
        return MAIL_TYPE;
    }
}
