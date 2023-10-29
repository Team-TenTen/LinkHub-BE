package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import com.tenten.linkhub.domain.space.repository.spacemember.SpaceMemberRepository;
import com.tenten.linkhub.domain.space.service.dto.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tenten.linkhub.domain.space.model.space.Role.OWNER;

@Service
public class DefaultSpaceService implements SpaceService{

    private final SpaceRepository spaceRepository;
    private final SpaceMemberRepository spaceMemberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SpaceMapper mapper;

    public DefaultSpaceService(SpaceRepository spaceRepository, SpaceMemberRepository spaceMemberRepository, ApplicationEventPublisher eventPublisher, SpaceMapper mapper) {
        this.spaceRepository = spaceRepository;
        this.spaceMemberRepository = spaceMemberRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request) {
        Slice<SpaceWithSpaceImage> spaces = spaceRepository.findByQuery(mapper.toQueryCond(request));

        return SpacesFindByQueryResponses.from(spaces);
    }

    @Override
    @Transactional
    public Long createSpace(SpaceCreateRequest request) {
        Space savedSpace = spaceRepository.save(mapper.toSpace(request));

        spaceMemberRepository.save(
                mapper.toSpaceMember(request, OWNER)
        );

        return savedSpace.getId();
    }

}
