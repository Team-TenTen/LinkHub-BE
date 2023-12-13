package com.tenten.linkhub;

import com.tenten.linkhub.global.aws.s3.ImageFileUploader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationApplicationTest {

    @MockBean
    protected ImageFileUploader mockImageFileUploader;

}
