package com.tenten.linkhub.domain.space.repository.link.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LinkViewDto {
    private String memberName;
    private String memberProfileImage;

    public LinkViewDto(String memberName, String memberProfileImage) {
        this.memberName = memberName;
        this.memberProfileImage = memberProfileImage;
    }
}
