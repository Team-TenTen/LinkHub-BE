package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.link.LinkRepository;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.tag.TagRepository;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinkUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DefaultLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final SpaceRepository spaceRepository;

    public DefaultLinkService(LinkRepository linkRepository, TagRepository tagRepository, SpaceRepository spaceRepository) {
        this.linkRepository = linkRepository;
        this.tagRepository = tagRepository;
        this.spaceRepository = spaceRepository;
    }

    @Override
    @Transactional
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
    public Long updateLink(LinkUpdateRequest request) {
        Space space = spaceRepository.getById(request.spaceId());
        Link link = linkRepository.getById(request.linkId());
        Optional<Tag> tag = toTag(space, link, request.tag());

        link.updateLink(new Url(request.url()), request.title(), tag);

        return link.getId();
    }

    private Optional<Tag> toTag(Space space, Link link, String tagName) {
        if (Objects.nonNull(tagName)) {
            return Optional.of(Tag.toTag(space, link, tagName));
        }
        return Optional.empty();
    }
}
