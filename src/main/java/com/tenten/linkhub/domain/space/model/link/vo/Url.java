package com.tenten.linkhub.domain.space.model.link.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url {

    private String url;

    public Url(String url) {
        Assert.notNull(url, "url은 null일 수 없습니다.");
        this.url = url;
    }

}
