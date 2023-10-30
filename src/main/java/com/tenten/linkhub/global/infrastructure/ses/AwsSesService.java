package com.tenten.linkhub.global.infrastructure.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.tenten.linkhub.global.util.email.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class AwsSesService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine templateEngine;

    public AwsSesService(AmazonSimpleEmailService amazonSimpleEmailService,
                         TemplateEngine templateEngine) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.templateEngine = templateEngine;
    }

    /**
     * 범용 이메일 발송 메소드
     */
    public void sendEmail(final EmailDto emailDto, final Map<String, Object> variables) {
        String content = templateEngine.process("email", createContext(variables));
        final SendEmailRequest sendEmailRequest = emailDto.toSendEmailRequest(content);

        amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }

    /**
     * 인증번호 발송 메소드
     */
    public void sendVerificationCodeEmail(final EmailDto emailDto, final String authKey) {
        final Map<String, Object> variables = new HashMap<>();
        variables.put("authKey", authKey);

        String content = templateEngine.process("email", createContext(variables));
        final SendEmailRequest sendEmailRequest = emailDto.toSendEmailRequest(content);

        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(sendEmailRequest);
        checkSendEmailSuccess(sendEmailResult);
    }

    private Context createContext(Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return context;
    }

    private void checkSendEmailSuccess(final SendEmailResult sendEmailResult) {
        if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            log.error("[AWS SES] 메일전송 중 에러 발생: {}", sendEmailResult.getSdkResponseMetadata().toString());
        }
    }
}




