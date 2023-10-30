package com.tenten.linkhub.global.util.email;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class EmailDto {

    private static final String FROM = "TeamLinkHub@link-hub.site";
    private static final String VERIFICATION_EMAIL_SUBJECT = "링크허브 이메일 인증 코드 발송";

    private final List<String> to; //받는 사람
    private final String subject; //제목

    public static EmailDto toVerificationEmailDto(String to) {
        return new EmailDto(List.of(to), VERIFICATION_EMAIL_SUBJECT);
    }

    public SendEmailRequest toSendEmailRequest(String content) {
        final Destination destination = new Destination()
                .withToAddresses(this.to);

        final Message message = new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body().withHtml(createContent(content)));

        return new SendEmailRequest()
                .withSource(FROM)
                .withDestination(destination)
                .withMessage(message);
    }

    private EmailDto(
            final List<String> to,
            final String subject
    ) {
        this.to = to;
        this.subject = subject;
    }

    private Content createContent(final String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }

}
