package com.tenten.linkhub.domain.space.repository.tag;

import com.tenten.linkhub.domain.space.model.link.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.space.id= :spaceId AND t.name= :tagName")
    Optional<Tag> findTagBySpaceIdAndTagName(Long spaceId, String tagName);
}
