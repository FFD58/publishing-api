package ru.fafurin.publishing.model;

public enum Position {

    EDITOR("Редактор"),
    IMPOSER("Верстальщик"),
    ;

    private final String title;

    Position(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
