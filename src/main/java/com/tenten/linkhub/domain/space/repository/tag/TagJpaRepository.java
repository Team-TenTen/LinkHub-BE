package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.model.link.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t.name FROM Tag t WHERE t.space.id = :spaceId GROUP BY t.name")
    List<String> findBySpaceIdAndGroupBySpaceName(Long spaceId);
}
