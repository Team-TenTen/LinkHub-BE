package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.service.CommentService;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountDto;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentFacade {

    private final CommentService commentService;
    private final MemberService memberService;

    public CommentFacade(CommentService commentService, MemberService memberService) {
        this.commentService = commentService;
        this.memberService = memberService;
    }

    public CommentAndChildCountAndMemberInfoResponses findRootComments(Long spaceId, Pageable pageable) {
        CommentAndChildCountResponses commentAndChildCount = commentService.findRootComments(spaceId, pageable);

        List<Long> memberIds = getMemberIds(commentAndChildCount);

        MemberInfos memberInfos = memberService.findMemberInfosByMemberIds(memberIds);

        return CommentAndChildCountAndMemberInfoResponses.of(commentAndChildCount, memberInfos);
    }

    private List<Long> getMemberIds(CommentAndChildCountResponses rootCommentResponses) {
        return rootCommentResponses.responses().getContent()
                .stream()
                .map(CommentAndChildCountDto::memberId)
                .toList();
    }
}
