package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.space.service.SpaceService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SpaceFacade {

    private final SpaceService spaceService;
    private final ApplicationEventPublisher eventPublisher;


}
