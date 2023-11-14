package com.tenten.linkhub.domain.space.model.link;

public enum Color {

    EMERALD("emerald"),
    RED("red"),
    YELLOW("yellow"),
    BLUE("blue"),
    INDIGO("indigo"),
    PURPLE("purple"),
    PINK("pink"),
    GRAY("gray");

    private final String value;

    public String getValue() {
        return value;
    }

    Color(String value) {
        this.value = value;
    }
}
