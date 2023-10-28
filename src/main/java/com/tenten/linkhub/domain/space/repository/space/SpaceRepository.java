package com.tenten.linkhub.domain.space.repository.space;

import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.dto.QueryCond;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface SpaceRepository {
    Slice<Space> findByQuery(QueryCond queryCond);
}
