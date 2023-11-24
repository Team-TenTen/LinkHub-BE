package com.tenten.linkhub.domain.member.controller.mapper;

import com.tenten.linkhub.domain.member.controller.dto.MailSendApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberFollowCreateApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberJoinApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberMyProfileApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberProfileApiResponse;
import com.tenten.linkhub.domain.member.controller.dto.MemberSearchApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberSpacesFindApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberUpdateApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MemberUpdateApiResponse;
import com.tenten.linkhub.domain.member.service.dto.MailSendResponse;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberFollowCreateResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberJoinRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberMyProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberProfileResponse;
import com.tenten.linkhub.domain.member.service.dto.MemberSearchRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberUpdateRequest;
import com.tenten.linkhub.domain.member.service.dto.MemberUpdateResponse;
import com.tenten.linkhub.domain.space.service.dto.space.MemberSpacesFindRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MemberApiMapper {

    MailSendApiResponse toMailSendApiResponse(MailSendResponse response);

    MailVerificationRequest toMailVerificationRequest(MailVerificationApiRequest request);

    MailVerificationApiResponse toMailVerificatonApiResponse(MailVerificationResponse response);

    MemberJoinRequest toMemberJoinRequest(MemberJoinApiRequest request, MultipartFile file);

    MemberProfileApiResponse toMemberProfileApiResponse(MemberProfileResponse memberProfileResponse);

    MemberFollowCreateApiResponse toMemberFollowCreateApiResponse(
            MemberFollowCreateResponse memberFollowCreateResponse);

    MemberMyProfileApiResponse toMemberMyProfileApiResponse(MemberMyProfileResponse memberMyProfileResponse);

    MemberSpacesFindRequest toMemberSpacesFindRequest(Pageable pageable, MemberSpacesFindApiRequest request,
            Long requestMemberId, Long targetMemberId);

    MemberUpdateRequest toMemberUpdateRequest(MemberUpdateApiRequest request, MultipartFile file, Long targetMemberId,
            Long requestMemberId);

    MemberUpdateApiResponse toMemberUpdateApiResponse(MemberUpdateResponse memberUpdateResponse);

    MemberSpacesFindRequest toMemberSpacesFindRequest(Pageable pageable, MemberSpacesFindApiRequest request,
            Long requestMemberId, Long targetMemberId);

    MemberSearchRequest toMemberSearchRequest(MemberSearchApiRequest request, Pageable pageable, Long myMemberId);
}
