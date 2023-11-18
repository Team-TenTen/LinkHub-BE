package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.space.facade.dto.LinkCreateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.dto.LinkUpdateFacadeRequest;
import com.tenten.linkhub.domain.space.facade.mapper.LinkFacadeMapper;
import com.tenten.linkhub.domain.space.handler.dto.LinkDecreaseLikeCountEvent;
import com.tenten.linkhub.domain.space.handler.dto.LinkIncreaseLikeCountEvent;
import com.tenten.linkhub.domain.space.service.LinkService;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.link.LinkCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.link.LinkUpdateRequest;
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

        spaceService.checkMemberEditLink(memberId, spaceId); //링크 생성 권한 확인

        LinkCreateRequest request = mapper.toLinkCreateRequest(
                facadeRequest,
                memberId,
                spaceId
        );
        return linkService.createLink(request);
    }

    public long updateLink(long spaceId,
                           long linkId,
                           Long memberId,
                           LinkUpdateFacadeRequest facadeRequest) {

        spaceService.checkMemberEditLink(memberId, spaceId); //링크 수정 권한 확인

        LinkUpdateRequest request = mapper.toLinkUpdateRequest(
                facadeRequest,
                memberId,
                spaceId,
                linkId
        );
        return linkService.updateLink(request);
    }

    public Boolean createLike(Long linkId, Long memberId) {
        Boolean isLiked = linkService.createLike(linkId, memberId);

        eventPublisher.publishEvent(
                new LinkIncreaseLikeCountEvent(linkId)
        );

        return isLiked;
    }

    public void cancelLike(Long linkId, Long memberId) {
        linkService.cancelLike(linkId, memberId);

        eventPublisher.publishEvent(
                new LinkDecreaseLikeCountEvent(linkId)
        );
    }
}
