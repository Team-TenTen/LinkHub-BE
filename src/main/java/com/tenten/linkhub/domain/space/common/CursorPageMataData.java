package com.tenten.linkhub.domain.space.common;

public record CursorPageMataData(
        Long lastFavoriteCount,
        Long lastId,
        Integer pageSize,
        Boolean hasNext
) {
}
