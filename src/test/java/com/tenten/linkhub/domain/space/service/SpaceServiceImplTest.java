package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpaceServiceImplTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @BeforeEach
    void setUp(){
        setupData();
    }

    @Test
    @DisplayName("유저는 올바른 키워드, 필터, 정렬 조건들을 통해 Space를 검색할 수 있다.")
    void findSpacesByQuery(){
        //given
        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by("created_at").descending());

        SpacesFindByQueryRequest request = new SpacesFindByQueryRequest(pageRequest,
                "첫번째",
                Category.KNOWLEDGE_ISSUE_CAREER);

        //when
        SpacesFindByQueryResponses responses = spaceService.findSpacesByQuery(request);

        //then
        List<SpacesFindByQueryResponse> content = responses.responses().getContent();

        assertThat(content.get(0).spaceName()).isEqualTo("첫번째 스페이스");
        assertThat(content.get(0).description()).isEqualTo("첫번째 스페이스 소개글");
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
        assertThat(content.get(0).category()).isEqualTo(Category.KNOWLEDGE_ISSUE_CAREER);
    }

    private void setupData() {
        Space space1 = new Space(
                1L,
                "첫번째 스페이스",
                "첫번째 스페이스 소개글",
                Category.KNOWLEDGE_ISSUE_CAREER,
                true,
                true,
                true,
                true
        );

        Space space2 = new Space(
                1L,
                "두번째 스페이스",
                "두번째 스페이스 소개글",
                Category.ENTER_ART,
                true,
                true,
                true,
                true
        );

        spaceJpaRepository.save(space1);
        spaceJpaRepository.save(space2);
    }

}