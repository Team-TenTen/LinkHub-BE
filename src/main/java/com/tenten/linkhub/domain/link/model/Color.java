package com.tenten.linkhub.domain.link.model;

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

    Color(String value) {
        this.value = value;
    }

    public static Color toColor(String value) {
        for (Color color : Color.values()) {
            if (color.getValue().equals(value)) {
                return color;
            }
        }
        throw new IllegalArgumentException("정의된 태그의 색이 아닙니다.");
    }

    public String getValue() {
        return value;
    }

}
