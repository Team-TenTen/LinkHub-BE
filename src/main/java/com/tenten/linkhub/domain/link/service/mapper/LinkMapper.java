package com.tenten.linkhub.domain.link.service.mapper;

import com.tenten.linkhub.domain.link.model.Link;
import com.tenten.linkhub.domain.link.model.Tag;
import com.tenten.linkhub.domain.link.repository.link.dto.LinkGetQueryCondition;
import com.tenten.linkhub.domain.link.service.dto.LinksGetByQueryRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class LinkMapper {

    public LinkGetQueryCondition toQueryCondition(LinksGetByQueryRequest request) {
        return new LinkGetQueryCondition(
                request.spaceId(),
                request.memberId(),
                request.pageable(),
                request.tagId()
        );
    }

    public Map<Long, Long> toCopyAndPasteLinkIdMap(List<Link> sourceLinks, Long insertedLinksFirstId) {
        return IntStream.range(0, sourceLinks.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> sourceLinks.get(i).getId(),
                        i -> insertedLinksFirstId + i
                ));
    }

    public Map<Long, Long> toCopyAndPasteTagIdMap(List<Tag> sourceTags, Long insertedTagsFirstId) {
        return IntStream.range(0, sourceTags.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> sourceTags.get(i).getId(),
                        i -> insertedTagsFirstId + i
                ));
    }

}
