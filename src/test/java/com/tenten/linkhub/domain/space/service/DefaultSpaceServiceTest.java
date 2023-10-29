package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.domain.space.model.category.Category;
import com.tenten.linkhub.domain.space.model.space.Space;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.domain.space.repository.space.SpaceJpaRepository;
import com.tenten.linkhub.domain.space.repository.spaceimage.SpaceImageJpaRepository;
import com.tenten.linkhub.domain.space.service.dto.SpaceCreateRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryRequest;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponse;
import com.tenten.linkhub.domain.space.service.dto.SpacesFindByQueryResponses;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DefaultSpaceServiceTest {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private SpaceJpaRepository spaceJpaRepository;

    @Autowired
    private SpaceImageJpaRepository spaceImageJpaRepository;

    @MockBean
    private S3Uploader s3Uploader;

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

    @Test
    @DisplayName("유저는 스페이스를 생성할 수 있다.")
    void createSpace(){
        //given
        MockMultipartFile requestFile = new MockMultipartFile("테스트 이미지3", (byte[]) null);
        ImageInfo imageInfo = ImageInfo.of("https://testimage3", requestFile.getName());
        BDDMockito.given(s3Uploader.saveImage(any())).willReturn(imageInfo);

        SpaceCreateRequest spaceCreateRequest = new SpaceCreateRequest(
                "테스트용 스페이스 이름",
                "테스트용 스페이스 소개글",
                Category.ENTER_ART,
                true,
                true,
                true,
                true,
                3L,
                requestFile
        );

        //when
        Long savedSpaceId = spaceService.createSpace(spaceCreateRequest);

        //then
        Space savedSpace = spaceJpaRepository.findById(savedSpaceId).get();
        SpaceImage spaceImage = spaceImageJpaRepository.findSpaceImageBySpaceId(savedSpaceId).get();

        assertThat(savedSpace.getSpaceName()).isEqualTo("테스트용 스페이스 이름");
        assertThat(savedSpace.getDescription()).isEqualTo("테스트용 스페이스 소개글");
        assertThat(savedSpace.getCategory()).isEqualTo(Category.ENTER_ART);
        assertThat(spaceImage.getPath()).isEqualTo("https://testimage3");
        assertThat(spaceImage.getName()).isEqualTo("테스트 이미지3");
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

        spaceImageJpaRepository.save(
                new SpaceImage(space1, "https://testimage1", "테스트 이미지1")
        );

        spaceImageJpaRepository.save(
                new SpaceImage(space2, "https://testimage2", "테스트 이미지2")
        );
    }

}