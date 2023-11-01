package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.space.SpacesFindByQueryResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DefaultSpaceServiceTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @BeforeEach
    void setUp() {
        setupData();
    }

    @Test
    @DisplayName("유저는 올바른 키워드, 필터, 정렬 조건들을 통해 Space를 검색할 수 있다.")
    void findSpacesByQuery() {
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
        assertThat(content.get(0).spaceImagePath()).isEqualTo("https://testimage1");
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

        space1.addSpaceMember(
                new SpaceMember(1L, Role.OWNER)
        );

        space2.addSpaceMember(
                new SpaceMember(1L, Role.OWNER)
        );

        space1.addSpaceImage(
                new SpaceImage("https://testimage1", "테스트 이미지1")
        );

        space2.addSpaceImage(
                new SpaceImage( "https://testimage2", "테스트 이미지2")
        );

        spaceJpaRepository.save(space1);
        spaceJpaRepository.save(space2);
    }

}