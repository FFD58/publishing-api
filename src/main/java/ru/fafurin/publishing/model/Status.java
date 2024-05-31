package ru.fafurin.publishing.model;

public enum Status {

    AWAIT("Ожидает"),
    IN_WORK("В работе"),
    COMPLETED("Завершен"),
    ;

    private final String title;

    Status(String title) {
        this.title = title;
    }
}
