package com.tenten.linkhub.domain.member.controller.dto;

import com.tenten.linkhub.domain.member.model.Provider;
import com.tenten.linkhub.domain.space.model.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberJoinApiRequest(
        @Schema(title = "소셜 아이디", example = "342342384312")
        @NotBlank(message = "소셜 아이디는 빈 값이 들어올 수 없습니다.") String socialId,

        @Schema(title = "프로바이더", example = "kakao")
        @NotNull(message = "프로바이더는 빈 값이 들어올 수 없습니다.") Provider provider,

        @Schema(title = "닉네임", example = "프롱이")
        @NotBlank(message = "닉네임은 빈 값이 들어올 수 없습니다.") String nickname,

        @Schema(title = "한줄 소개", example = "만나서 반갑습니다.")
        @NotBlank(message = "한줄 소개는 빈 값이 들어올 수 없습니다.") String aboutMe,

        @Schema(title = "이메일", example = "frong@gmail.com")
        @NotBlank(message = "이메일은 빈 값이 들어올 수 없습니다.") String newsEmail,

        @Schema(title = "관심 카테고리", example = "KNOWLEDGE_ISSUE_CAREER")
        @NotNull(message = "관심 카테고리는 빈 값이 들어올 수 없습니다.") Category favoriteCategory,

        @Schema(title = "뉴스레터 동의", example = "true")
        @NotNull(message = "동의 여부는 빈 값이 들어올 수 없습니다.") Boolean isSubscribed) {

}
