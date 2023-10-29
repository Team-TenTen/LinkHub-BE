package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.SpaceRepository;
import com.tenten.linkhub.domain.space.repository.space.dto.SpaceWithSpaceImage;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import com.tenten.linkhub.domain.space.service.mapper.SpaceMapper;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultSpaceService implements SpaceService{

    private final SpaceRepository spaceRepository;
    private final SpaceMapper mapper;

    public DefaultSpaceService(SpaceRepository spaceRepository, SpaceMapper mapper) {
        this.spaceRepository = spaceRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public SpacesFindByQueryResponses findSpacesByQuery(SpacesFindByQueryRequest request) {
        Slice<SpaceWithSpaceImage> spaces = spaceRepository.findByQuery(mapper.toQueryCond(request));

        return SpacesFindByQueryResponses.from(spaces);
    }
}
