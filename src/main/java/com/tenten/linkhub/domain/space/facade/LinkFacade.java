package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.mapper.LinkFacadeMapper;
import com.tenten.linkhub.domain.space.handler.dto.LinkDecreaseLikeCountDto;
import com.tenten.linkhub.domain.space.handler.dto.LinkIncreaseLikeCountDto;
import com.tenten.linkhub.domain.space.service.LinkService;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class LinkFacade {
    private final SpaceService spaceService;
    private final LinkService linkService;
    private final LinkFacadeMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public LinkFacade(SpaceService spaceService, LinkService linkService, LinkFacadeMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.spaceService = spaceService;
        this.linkService = linkService;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    public Long createLink(Long spaceId,
                           Long memberId,
                           LinkCreateFacadeRequest facadeRequest) {

        spaceService.checkMemberAddLink(memberId, spaceId); //링크 생성 권한 확인

        LinkCreateRequest request = mapper.toLinkCreateRequest(
                facadeRequest,
                memberId,
                spaceId
        );
        return linkService.createLink(request);
    }

    public Boolean createLike(Long linkId, Long memberId) {
        Boolean isLiked = linkService.createLike(linkId, memberId);

        eventPublisher.publishEvent(
                new LinkIncreaseLikeCountDto(linkId)
        );

        return isLiked;
    }

    public void cancelLike(Long linkId, Long memberId) {
        linkService.cancelLike(linkId, memberId);

        eventPublisher.publishEvent(
                new LinkDecreaseLikeCountDto(linkId)
        );
    }
}
