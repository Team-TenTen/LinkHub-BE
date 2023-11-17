package com.tenten.linkhub.domain.member.controller.mapper;

import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowCreateApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowersFindApiResponses;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowingsFindApiResponses;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberMyProfileApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberProfileApiResponse;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowCreateResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowersFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowingsFindResponses;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberMyProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MemberApiMapper {

    MailVerificationRequest toMailVerificationRequest(MailVerificationApiRequest request);

    MailVerificationApiResponse toMailVerificatonApiResponse(MailVerificationResponse response);

    MemberJoinRequest toMemberJoinRequest(MemberJoinApiRequest request, MultipartFile file);

    MemberProfileApiResponse toMemberProfileApiResponse(MemberProfileResponse memberProfileResponse);

    MemberFollowCreateApiResponse toMemberFollowCreateApiResponse(MemberFollowCreateResponse memberFollowCreateResponse);

    MemberFollowersFindApiResponses toMemberFollowersFindApiResponses(MemberFollowersFindResponses memberFollowersFindResponses);

    MemberMyProfileApiResponse toMemberMyProfileApiResponse(MemberMyProfileResponse memberMyProfileResponse);
}
