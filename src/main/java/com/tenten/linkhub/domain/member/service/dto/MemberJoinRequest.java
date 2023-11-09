package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.space.model.category.Category;
import org.springframework.web.multipart.MultipartFile;

public record MemberJoinRequest(
        String socialId,
        Provider provider,
        String nickname,
        String aboutMe,
        String newsEmail,
        Category favoriteCategory,
        Boolean isSubscribed,
        MultipartFile file
) {

}
