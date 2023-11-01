package com.tenten.linkhub.domain.member.controller.mapper;

import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiRequest;
import com.tenten.linkhub.domain.member.controller.dto.MailVerificationApiResponse;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationRequest;
import com.tenten.linkhub.domain.member.service.dto.MailVerificationResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MemberApiMapper {

    MailVerificationRequest toMailVerificationRequest(MailVerificationApiRequest request);

    MailVerificationApiResponse toMailVerificatonApiResponse(MailVerificationResponse response);

}
