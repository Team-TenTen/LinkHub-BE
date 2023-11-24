package com.tenten.linkhub.domain.space.repository.link;

import com.tenten.linkhub.domain.space.model.link.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Repository
public class LinkJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long bulkInsertLinks(List<Link> sourceLinks, Long spaceId, Long memberId) {
        String sql = "INSERT INTO links (space_id, member_id, title, url, like_count, created_at, updated_at, is_deleted, version) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        jdbcTemplate.batchUpdate(sql,
                sourceLinks,
                sourceLinks.size(),
                (PreparedStatement ps, Link link) -> {
                    ps.setLong(1, spaceId);
                    ps.setLong(2, memberId);
                    ps.setString(3, link.getTitle());
                    ps.setString(4, link.getUrl().getUrl());
                    ps.setLong(5, link.getLikeCount());
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setBoolean(8, false);
                    ps.setLong(9, 0L);
                });

        return jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);
    }

}
