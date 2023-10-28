package com.tenten.linkhub.domain.space.util;

import lombok.Getter;

@Getter
public enum Sort {
    POPULARITY("favorite_count"),
    CREATED_AT("created_at");

    private final String discription;

    Sort(String discription) {
        this.discription = discription;
    }

}
