package ru.fafurin.publishing.service;

import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;

import java.util.HashMap;

public interface MailService {
    void sendEmail(Customer customer, MailType type, HashMap<String, Object> props);
}
