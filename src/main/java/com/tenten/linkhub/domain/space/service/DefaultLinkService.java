package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.link.Like;
import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.like.LikeRepository;
import com.tenten.linkhub.domain.space.repository.link.LinkRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.tag.TagRepository;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class DefaultLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final SpaceRepository spaceRepository;
    private final LikeRepository likeRepository;

    public DefaultLinkService(LinkRepository linkRepository, TagRepository tagRepository, SpaceRepository spaceRepository, LikeRepository likeRepository) {
        this.linkRepository = linkRepository;
        this.tagRepository = tagRepository;
        this.spaceRepository = spaceRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public Long createLink(LinkCreateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());

        Link link = Link.toLink(
                space,
                request.memberId(),
                request.title(),
                new Url(request.url()));

        if (Objects.nonNull(request.tag())) {
            Tag tag = Tag.toTag(space, link, request.tag());
            link.addTag(tag);
        }
        return linkRepository.save(link).getId();
    }

    @Override
    @Transactional
    public Boolean createLike(Long linkId, Long memberId) {
        Link link = linkRepository.findById(linkId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 링크입니다."));

        likeRepository.findByLinkIdAndMemberId(linkId, memberId)
                .ifPresent(m -> {
                    throw new UnauthorizedAccessException("이미 좋아요한 링크입니다.");
                });

        likeRepository.save(new Like(link, memberId));

        return true;
    }

    @Override
    @Transactional
    public void cancelLike(Long linkId, Long memberId) {
        Optional<Like> like = likeRepository.findByLinkIdAndMemberId(linkId, memberId);

        like.ifPresent(likeRepository::delete);
    }
}
