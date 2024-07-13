package ru.fafurin.publishing.service.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для отправки электронных писем
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final JavaMailSender mailSender;

    private final Map<MailType, MailGenerator> mailGenerators = new HashMap<>();
    /**
     * Зарегистрировать объект, отвечающий за отправку письма
     *
     * @param type - тип письма
     * @param generator - объект, отвечающий за отправку письма в зависимости от типа письма
     */
    public void register(MailType type, MailGenerator generator) {
        mailGenerators.put(type, generator);
    }

    /**
     * Отправить письмо.
     *
     * @param customer - заказчик
     * @param type - тип письма
     * @param props - дополнительные данные
     */
    @Override
    public void send(Customer customer, MailType type, HashMap<String, Object> props) {
        MimeMessage message = mailSender.createMimeMessage();
        MailGenerator mailGenerator = mailGenerators.get(type);
        if (mailGenerator != null) {
            MimeMessage generatedMessage = mailGenerator.generate(message, customer, props);
            mailSender.send(generatedMessage);
            log.info("Отчет для заказчика: {} отправлен на адрес: {}", customer.getName(), customer.getEmail());
        }
    }
}
