package com.tenten.linkhub.domain.space.model.link.vo;

import jakarta.persistence.Column;
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

    @Column(length = 2083, nullable = false)
    private String url;

    public Url(String url) {
        validateNotNull(url, "url");
        validateMaxSize(url, 2083, "url");
        this.url = url;
    }

}
