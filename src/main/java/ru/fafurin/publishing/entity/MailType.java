package ru.fafurin.publishing.entity;

public enum MailType {
    /**
     * Используется для отправки email заказчику при создании заказа
     */
    ORDER_CREATE,
    /**
     * Используется для отправки email заказчику с отчетом по заказу
     * когда до дедлайна остается 30 дней
     */
    ORDER_REPORT
}
