package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.web.multipart.MultipartFile;

public record MemberUpdateRequest(
        String nickname,
        String aboutMe,
        String newsEmail,
        Category favoriteCategory,
        Boolean isSubscribed,
        MultipartFile file,
        Long targetMemberId,
        Long requestMemberId
) {

}
