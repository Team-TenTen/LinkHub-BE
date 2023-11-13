package com.tenten.linkhub.domain.space.facade.dto;

import com.tenten.linkhub.domain.member.service.dto.MemberInfo;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountResponses;
import org.springframework.data.domain.Slice;

import java.util.Map;
import java.util.Objects;

public record CommentAndChildCountAndMemberInfoResponses(Slice<CommentAndChildCountAndMemberInfo> responses) {

    public static CommentAndChildCountAndMemberInfoResponses of(CommentAndChildCountResponses commentAndChildCount, MemberInfos memberDetailInfos){
        Map<Long, MemberInfo> memberInfos = memberDetailInfos.memberInfos();

        Slice<CommentAndChildCountAndMemberInfo> mapResponses = commentAndChildCount.responses()
                .map(c -> {
                    MemberInfo memberInfo = memberInfos.get(c.memberId());

                    return new CommentAndChildCountAndMemberInfo(
                            c.commentId(),
                            c.content(),
                            c.createdAt(),
                            c.updatedAt(),
                            c.childCount(),
                            c.memberId(),
                            Objects.isNull(memberInfo) ? null : memberInfo.nickname(),
                            Objects.isNull(memberInfo) ? null : memberInfo.aboutMe(),
                            Objects.isNull(memberInfo) ? null : memberInfo.path()
                    );
                });

        return new CommentAndChildCountAndMemberInfoResponses(mapResponses);
    }

}
