package com.tenten.linkhub.domain.space.service.mapper;

import com.tenten.linkhub.domain.space.model.link.Link;
import com.tenten.linkhub.domain.space.model.link.LinkTag;
import com.tenten.linkhub.domain.space.model.link.Tag;
import com.tenten.linkhub.domain.space.repository.link.dto.LinkGetQueryCondition;
import com.tenten.linkhub.domain.space.service.dto.link.LinksGetByQueryRequest;
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

    public Map<Long, Long> toCopedAndPasteLinkIdMap(List<Link> targetLinks, Long insertedLinksFirstId) {
        return IntStream.range(0, targetLinks.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> targetLinks.get(i).getId(),
                        i -> insertedLinksFirstId + i
                ));
    }

    public Map<Long, Long> toCopedAndPasteTagIdMap(List<Tag> targetTags, Long insertedTagsFirstId) {
        return IntStream.range(0, targetTags.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> targetTags.get(i).getId(),
                        i -> insertedTagsFirstId + i
                ));
    }

}
