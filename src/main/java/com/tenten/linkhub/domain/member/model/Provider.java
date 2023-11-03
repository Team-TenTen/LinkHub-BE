package com.tenten.linkhub.domain.member.model;

import lombok.Getter;

@Getter
public enum Provider {

    kakao("kakao");

    private final String value;

    Provider(String value) {
        this.value = value;
    }

}