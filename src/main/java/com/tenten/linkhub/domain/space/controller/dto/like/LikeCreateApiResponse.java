package com.tenten.linkhub.domain.space.controller.dto.like;

public record LikeCreateApiResponse(Boolean isLiked) {
    public static LikeCreateApiResponse from(Boolean isLiked) {
        return new LikeCreateApiResponse(isLiked);
    }
}
