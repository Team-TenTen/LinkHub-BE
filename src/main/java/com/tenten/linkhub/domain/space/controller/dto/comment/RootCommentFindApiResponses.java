package com.tenten.linkhub.domain.space.controller.dto.comment;

import com.tenten.linkhub.domain.space.facade.dto.CommentAndChildCountAndMemberInfoResponses;
import com.tenten.linkhub.global.util.PageMetaData;
import java.util.List;
import org.springframework.data.domain.Slice;

public record RootCommentFindApiResponses(
        List<RootCommentFindApiResponse> responses,
        PageMetaData metaData
) {
    public static RootCommentFindApiResponses from(CommentAndChildCountAndMemberInfoResponses responses){
        Slice<RootCommentFindApiResponse> mapResponses = responses.responses()
                .map(r -> new RootCommentFindApiResponse(
                        r.commentId(),
                        r.content(),
                        r.createdAt(),
                        r.updatedAt(),
                        r.childCount(),
                        r.memberId(),
                        r.nickname(),
                        r.aboutMe(),
                        r.profileImagePath(),
                        r.isModifiable()
                ));

        PageMetaData pageMetaData = new PageMetaData(
                mapResponses.hasNext(),
                mapResponses.getSize(),
                mapResponses.getNumber());

        return new RootCommentFindApiResponses(
                mapResponses.getContent(),
                pageMetaData);
    }

}
