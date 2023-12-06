package com.tenten.linkhub.global.util;

public record PageMetaData(
        Boolean hasNext,
        Integer pageSize,
        Integer pageNumber
) {
}
