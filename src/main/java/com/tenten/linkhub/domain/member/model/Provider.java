package com.tenten.linkhub.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {

    KAKAO("KAKAO");

    private final String value;
}
