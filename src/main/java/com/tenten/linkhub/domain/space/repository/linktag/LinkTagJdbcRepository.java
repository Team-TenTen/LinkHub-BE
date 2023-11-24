package com.tenten.linkhub.domain.space.repository.linktag;

import com.tenten.linkhub.domain.space.model.link.LinkTag;
import com.tenten.linkhub.domain.space.model.link.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class LinkTagJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long bulkInsertLinkTag(List<LinkTag> targetLinkTags, Map<Long, Long> linkIdMappingMap, Map<Long, Long> tagIdMappingMap) {
        String sql = "INSERT INTO link_tags (link_id, tag_id, created_at, updated_at, is_deleted) " +
                "VALUES (?, ?, ?, ?, ?) ";

        jdbcTemplate.batchUpdate(sql,
                targetLinkTags,
                targetLinkTags.size(),
                (PreparedStatement ps, LinkTag linkTag) -> {
                    ps.setLong(1, linkIdMappingMap.get(linkTag.getLink().getId()));
                    ps.setLong(2, tagIdMappingMap.get(linkTag.getTag().getId()));
                    ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setBoolean(5, false);
                });

        return jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);
    }

}
