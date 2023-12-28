package com.tenten.linkhub.global.util;

import java.util.Objects;

public final class CommonValidator {
    private static final String NULL_MESSAGE = "%s는 null일 수 없습니다.";

    private CommonValidator() {
        throw new RuntimeException("CommonValidator객체는 생성할 수 없습니다.");
    }

    public static void validateNotNull(Object target, String targetName) {
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException(String.format(NULL_MESSAGE, targetName));
        }
    }

    public static void validateMaxSize(String value, int length, String valueName) {
        validateNotNull(value, valueName);
        if (value.length() > length) {
            throw new IllegalArgumentException(String.format("%s의 길이는 %s 이하여야 합니다.", valueName, length));
        }
    }

    public static void validateMinMaxSize(String value, int minSize, int maxSize, String valueName) {
        validateNotNull(value, valueName);

        int targetSize = value.length();
        if (targetSize < minSize || targetSize > maxSize) {
            throw new IllegalArgumentException(String.format("%s의 길이는 %s 이상 %s 이하 이여야 합니다.", valueName, minSize, maxSize));
        }
    }

}
