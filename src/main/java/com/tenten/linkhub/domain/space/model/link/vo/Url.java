package com.tenten.linkhub.domain.space.model.link.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.tenten.linkhub.global.util.CommonValidator.validateMaxSize;
import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url {

    private String url;

    public Url(String url) {
        validateNotNull(url, "url은 null일 수 없습니다.");
        validateMaxSize(url, 2083, "URL의 최대 길이는 2083자 입니다.");
        this.url = url;
    }

}
