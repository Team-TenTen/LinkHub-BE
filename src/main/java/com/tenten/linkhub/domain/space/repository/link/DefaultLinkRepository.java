package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetDto;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetQueryCondition;
import com.tenten.linkhub.domain.space.repository.link.dto.PopularLinkGetDto;
import com.tenten.linkhub.domain.space.repository.link.querydsl.LinkQueryDslRepository;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultLinkRepository implements LinkRepository {
    private final LinkJpaRepository linkJpaRepository;
    private final LinkQueryDslRepository linkQueryDslRepository;
    private final LinkJdbcRepository linkJdbcRepository;

    public DefaultLinkRepository(LinkJpaRepository linkJpaRepository,
                                 LinkQueryDslRepository linkQueryDslRepository,
                                 LinkJdbcRepository linkJdbcRepository) {
        this.linkJpaRepository = linkJpaRepository;
        this.linkQueryDslRepository = linkQueryDslRepository;
        this.linkJdbcRepository = linkJdbcRepository;
    }

    @Override
    public Link save(Link link) {
        return linkJpaRepository.save(link);
    }

    @Override
    public Link getById(Long linkId) {
        return linkJpaRepository
                .findById(linkId)
                .orElseThrow(() -> new DataNotFoundException("linkId에 해당하는 link를 찾을 수 없습니다."));
    }

    @Override
    public Optional<Link> findById(Long linkId) {
        return linkJpaRepository.findById(linkId);
    }

    @Override
    public Slice<LinkGetDto> getLinksByCondition(LinkGetQueryCondition condition) {
        return linkQueryDslRepository.getLinksByCondition(condition);
    }

    @Override
    public List<PopularLinkGetDto> getPopularLinks(Long memberId) {
        return linkQueryDslRepository.getPopularLinks(memberId);
    }

    @Override
    public Long countLinkBySpaceId(Long spaceId) {
        return linkJpaRepository.countLinkBySpaceIdAndIsDeletedFalse(spaceId);
    }

    @Override
    public List<Link> findBySpaceId(Long sourceSpaceId) {
        return linkJpaRepository.findBySpaceIdAndIsDeletedFalse(sourceSpaceId);
    }

    @Override
    public Long bulkInsertLinks(List<Link> sourceLinks, Long spaceId, Long memberId) {
        return linkJdbcRepository.bulkInsertLinks(sourceLinks, spaceId, memberId);
    }

}
