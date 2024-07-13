package ru.fafurin.publishing.service.mail;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;

import java.util.HashMap;

public interface MailGenerator {
    MimeMessage generate(MimeMessage message, Customer customer, HashMap<String, Object> props);

    MailType getMyType();

    @Autowired
    default void registerMyself(MailSenderImpl mailSender) {
        mailSender.register(getMyType(), this);
    }
}
