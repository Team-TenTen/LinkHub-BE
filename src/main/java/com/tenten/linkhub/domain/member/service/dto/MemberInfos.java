package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;

import java.util.List;
import java.util.Objects;

public record MemberInfos(List<MemberInfo> memberInfos) {

    public static MemberInfos from(List<Member> members){
        List<MemberInfo> memberInfoList = members.stream()
                .map(m -> new MemberInfo(
                        m.getId(),
                        m.getNickname(),
                        m.getAboutMe(),
                        Objects.isNull(m.getProfileImages().get(0)) ? null : m.getProfileImages().get(0).getPath()
                ))
                .toList();

        return new MemberInfos(memberInfoList);
    }

}
