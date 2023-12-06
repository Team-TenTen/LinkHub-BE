package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.space.model.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberUpdateApiRequest(

        @Schema(title = "닉네임", example = "프롱이")
        @NotBlank(message = "닉네임은 빈 값이 들어올 수 없습니다.") String nickname,

        @Schema(title = "한줄 소개", example = "만나서 반갑습니다.")
        String aboutMe,

        @Schema(title = "이메일", example = "frong@gmail.com")
        @NotBlank(message = "이메일은 빈 값이 들어올 수 없습니다.") String newsEmail,

        @Schema(title = "관심 카테고리", example = "KNOWLEDGE_ISSUE_CAREER")
        @NotNull(message = "관심 카테고리는 빈 값이 들어올 수 없습니다.") Category favoriteCategory,

        @Schema(title = "뉴스레터 동의", example = "true")
        @NotNull(message = "동의 여부는 빈 값이 들어올 수 없습니다.") Boolean isSubscribed) {

}
