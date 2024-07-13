package ru.fafurin.publishing.service.mail;

import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;

import java.util.HashMap;

public interface MailSender {
    void send(Customer customer, MailType type, HashMap<String, Object> props);
}
