package com.tenten.linkhub.global;

public record PageMetaData(
        Boolean hasNext,
        Integer pageSize,
        Integer pageNumber
) {
}
