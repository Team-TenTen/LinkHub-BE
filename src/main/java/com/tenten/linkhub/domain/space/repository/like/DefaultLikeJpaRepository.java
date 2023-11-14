package com.tenten.linkhub.domain.space.repository.like;

import com.tenten.linkhub.domain.space.model.link.Like;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultLikeJpaRepository implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    public DefaultLikeJpaRepository(LikeJpaRepository likeJpaRepository) {
        this.likeJpaRepository = likeJpaRepository;
    }

    @Override
    public Optional<Like> findByLinkIdAndMemberId(Long linkId, Long memberId) {
        return likeJpaRepository.findByLinkIdAndMemberId(linkId, memberId);
    }

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(like);
    }

    @Override
    public void delete(Like like) {
        likeJpaRepository.delete(like);
    }
}
