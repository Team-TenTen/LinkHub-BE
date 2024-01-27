package com.tenten.linkhub.domain.link.controller.dto;

public record LikeCreateApiResponse(Boolean isLiked) {
    public static LikeCreateApiResponse from(Boolean isLiked) {
        return new LikeCreateApiResponse(isLiked);
    }
}
