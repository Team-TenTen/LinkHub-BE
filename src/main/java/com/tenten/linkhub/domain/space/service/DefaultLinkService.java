package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.model.link.vo.Url;
import com.tenten.linkhub.domain.space.repository.link.LinkRepository;
import com.tenten.linkhub.domain.space.repository.tag.TagRepository;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;

    public DefaultLinkService(LinkRepository linkRepository, TagRepository tagRepository) {
        this.linkRepository = linkRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Long createLink(LinkCreateRequest request) {
        Link link = Link.toLink(
                request.space(),
                request.memberId(),
                request.title(),
                new Url(request.url()));

        if (Objects.nonNull(request.tag())) {
            Tag tag = Tag.toTag(request.space(), link, request.tag());
            link.addTag(tag);
        }
        return linkRepository.save(link).getId();
    }
}
