package com.tenten.linkhub.domain.space.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class SpaceCursorSlice<T> {
    private final Long lastFavoriteCount;
    private final Long lastId;
    private final Integer pageSize;
    private final Boolean hasNext;
    private final List<T> content;

    public SpaceCursorSlice(Long lastFavoriteCount, Long lastId, Integer pageSize, Boolean hasNext, List<T> content) {
        this.lastFavoriteCount = lastFavoriteCount;
        this.lastId = lastId;
        this.pageSize = pageSize;
        this.hasNext = hasNext;
        this.content = new ArrayList<>(content);
    }

    public static <T> SpaceCursorSlice<T> of(Long lastFavoriteCount, Long lastId, Integer pageSize, Boolean hasNext, List<T> content) {
        return new SpaceCursorSlice<>(
                lastFavoriteCount,
                lastId,
                pageSize,
                hasNext,
                content
        );
    }

    public <U> SpaceCursorSlice<U> map(Function<? super T, ? extends U> converter) {
        List<U> newContent = content.stream()
                .map(converter)
                .collect(Collectors.toList());
        return new SpaceCursorSlice<>(
                lastFavoriteCount,
                lastId,
                pageSize,
                hasNext,
                newContent
        );
    }

}
