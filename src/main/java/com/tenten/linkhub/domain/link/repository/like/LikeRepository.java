package com.tenten.linkhub.domain.link.repository.like;

import com.tenten.linkhub.domain.link.model.Like;

import java.util.Optional;

public interface LikeRepository {

    Optional<Like> findByLinkIdAndMemberId(Long linkId, Long memberId);

    Like save(Like like);

    void delete(Like like);
}
