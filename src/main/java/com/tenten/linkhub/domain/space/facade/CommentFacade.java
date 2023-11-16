package com.tenten.linkhub.domain.space.facade;

import com.tenten.linkhub.domain.member.service.MemberService;
import com.tenten.linkhub.domain.member.service.dto.MemberInfos;
import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.facade.dto.RepliesAndMemberInfoResponses;
import com.tenten.linkhub.domain.space.service.CommentService;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountDto;
import com.tenten.linkhub.domain.space.service.dto.comment.CommentAndChildCountResponses;
import com.tenten.linkhub.domain.space.service.dto.comment.RepliesFindResponse;
import com.tenten.linkhub.domain.space.service.dto.comment.RepliesFindResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentFacade {

    private final CommentService commentService;
    private final MemberService memberService;

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

    public RepliesAndMemberInfoResponses findReplies(Long spaceId, Long commentId, Pageable pageable) {
        RepliesFindResponses repliesFindResponses = commentService.findReplies(spaceId, commentId, pageable);

        List<Long> memberIds = getMemberIdsFromReplies(repliesFindResponses);

        MemberInfos memberInfos = memberService.findMemberInfosByMemberIds(memberIds);

        return RepliesAndMemberInfoResponses.of(repliesFindResponses, memberInfos);
    }

    private List<Long> getMemberIdsFromReplies(RepliesFindResponses repliesFindResponses) {
        return repliesFindResponses.responses().getContent()
                .stream()
                .map(RepliesFindResponse::memberId)
                .toList();
    }
}
