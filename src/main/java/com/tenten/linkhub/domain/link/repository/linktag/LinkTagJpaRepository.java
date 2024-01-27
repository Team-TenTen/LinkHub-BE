package com.tenten.linkhub.domain.link.repository.linktag;

import com.tenten.linkhub.domain.link.model.LinkTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinkTagJpaRepository extends JpaRepository<LinkTag, Long> {

    @Query("SELECT lt " +
            "FROM LinkTag lt " +
            "WHERE lt.link.id IN :linkIds AND lt.isDeleted = false ")
    List<LinkTag> findByLinkIdsInAndIsDeletedFalse(List<Long> linkIds);
}
