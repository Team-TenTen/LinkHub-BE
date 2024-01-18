package com.tenten.linkhub.domain.member.service.dto;

import com.tenten.linkhub.domain.member.model.Member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MemberNicknames(
        Map<Long, String> memberNicknames
) {
    public static MemberNicknames from(List<Member> members) {
        Map<Long, String> memberNicknames = members.stream()
                .collect(Collectors.toMap(
                        Member::getId,
                        Member::getNickname
                ));

        return new MemberNicknames(memberNicknames);
    }
}
