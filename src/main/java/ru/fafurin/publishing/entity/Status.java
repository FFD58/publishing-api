package ru.fafurin.publishing.entity;

public enum Status {

    AWAIT("Ожидает"),
    IN_WORK("В работе"),
    COMPLETED("Завершен"),
    ;

    private final String title;

    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
