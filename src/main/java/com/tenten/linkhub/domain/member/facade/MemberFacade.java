package com.tenten.linkhub.domain.member.facade;

import com.tenten.linkhub.domain.member.facade.dto.MemberSpacesFindByQueryFacadeResponses;
import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberNicknames;
import com.tenten.linkhub.domain.space.service.SpaceService;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberFacade {
    private final MemberService memberService;
    private final SpaceService spaceService;

    public MemberSpacesFindByQueryFacadeResponses findMemberSpacesByQuery(MemberSpacesFindRequest request) {
        SpacesFindByQueryResponses serviceResponses = spaceService.findMemberSpacesByQuery(request);

        List<Long> memberIds = getMemberIds(serviceResponses);
        MemberNicknames memberNicknames = memberService.findMemberNicknamesByMemberIds(memberIds);

        return MemberSpacesFindByQueryFacadeResponses.of(serviceResponses, memberNicknames);
    }

    private List<Long> getMemberIds(SpacesFindByQueryResponses serviceResponses) {
        return serviceResponses.responses().getContent().stream()
                .map(SpacesFindByQueryResponse::ownerId)
                .toList();
    }
}
