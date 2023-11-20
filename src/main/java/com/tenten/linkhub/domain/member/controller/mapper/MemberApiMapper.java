package com.tenten.linkhub.domain.member.controller.mapper;

import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberProfileApiResponse;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberSpacesFindApiRequest;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MemberApiMapper {

    MailVerificationRequest toMailVerificationRequest(MailVerificationApiRequest request);

    MailVerificationApiResponse toMailVerificatonApiResponse(MailVerificationResponse response);

    MemberJoinRequest toMemberJoinRequest(MemberJoinApiRequest request, MultipartFile file);

    MemberProfileApiResponse toMemberProfileApiResponse(MemberProfileResponse memberProfileResponse);

    MemberSpacesFindRequest toMemberSpacesFindRequest(Pageable pageable, MemberSpacesFindApiRequest request, Long requestMemberId, Long targetMemberId);
}
