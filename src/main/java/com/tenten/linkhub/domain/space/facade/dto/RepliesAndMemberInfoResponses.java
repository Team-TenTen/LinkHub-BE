package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberInfo;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.service.dto.comment.RepliesFindResponses;
import org.springframework.data.domain.Slice;

import java.util.Map;
import java.util.Objects;

public record RepliesAndMemberInfoResponses(Slice<RepliesAndMemberInfo> responses) {

    public static RepliesAndMemberInfoResponses of(RepliesFindResponses repliesFindResponses, MemberInfos memberDetailInfos){
        Map<Long, MemberInfo> memberInfos = memberDetailInfos.memberInfos();

        Slice<RepliesAndMemberInfo> mapResponses = repliesFindResponses.responses()
                .map(c -> {
                    MemberInfo memberInfo = memberInfos.get(c.memberId());

                    return new RepliesAndMemberInfo(
                            c.commentId(),
                            c.content(),
                            c.createdAt(),
                            c.updatedAt(),
                            c.memberId(),
                            Objects.isNull(memberInfo) ? null : memberInfo.nickname(),
                            Objects.isNull(memberInfo) ? null : memberInfo.aboutMe(),
                            Objects.isNull(memberInfo) ? null : memberInfo.path()
                    );
                });

        return new RepliesAndMemberInfoResponses(mapResponses);
    }

}
