package com.tenten.linkhub.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {

    kakao("kakao");

    private final String value;
}
