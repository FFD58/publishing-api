package ru.fafurin.publishing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.entity.MailType;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.service.MailService;
import ru.fafurin.publishing.service.OrderService;
import ru.fafurin.publishing.service.ReportService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderService orderService;
    private final MailService mailService;
    private final Duration DURATION = Duration.ofDays(30);

    /**
     * Проверить у каких заказов осталось 30 дней до дедлайна.
     * Если такие заказы есть, заказчикам отправляется отчет о проделанной работе
     */
    @Scheduled(cron = "00 10 * * * *")
    @Override
    public void reportOnOrder() {
        List<Order> orders = orderService.findAllSoonOrders(DURATION);
        HashMap<String, Object> props = new HashMap<>();
        orders.forEach(order -> {
            Customer customer = order.getCustomer();
            props.put("order", order);
            props.put("book", order.getBook());
            props.put("tasks", order.getTasks());
            mailService.sendEmail(customer, MailType.REPORT, props);
            order.setReported(true);
            orderService.refresh(order);
        });
    }
}
