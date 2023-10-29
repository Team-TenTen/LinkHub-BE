package com.tenten.linkhub.domain.space.repository.spaceimage;

import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpaceImageJpaRepository extends JpaRepository<SpaceImage, Long> {

    @Query("select si from SpaceImage si where si.space.id = :spaceId")
    Optional<SpaceImage> findSpaceImageBySpaceId(Long spaceId);
}
