package com.tenten.linkhub.domain.space.repository.like;

import com.tenten.linkhub.domain.space.model.link.Like;

import java.util.Optional;

public interface LikeRepository {

    Optional<Like> findByLinkIdAndMemberId(Long linkId, Long memberId);

    Like save(Like like);

    void delete(Like like);
}
