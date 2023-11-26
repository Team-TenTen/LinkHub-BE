package com.tenten.linkhub.domain.notification.model;

import com.tenten.linkhub.global.entity.BaseTimeEntity;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import com.tenten.linkhub.global.util.CommonValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "is_checked")
    private boolean isChecked;

    @Builder
    public Notification(Long recipientId, Long senderId, NotificationType notificationType) {
        validateNotNull(recipientId, "recipientId");
        validateNotNull(senderId, "senderId");
        validateNotNull(notificationType, "notificationType");

        this.recipientId = recipientId;
        this.senderId = senderId;
        this.notificationType = notificationType;
        this.isChecked = false;
    }

    public void changeIsCheckedAsTrue(Long memberId) {
        validateRecipient(memberId);

        isChecked = true;
    }

    public void validateRecipient(Long memberId) {
        if (!Objects.equals(recipientId, memberId)) {
            throw new UnauthorizedAccessException("해당 멤버는 이 알림의 수신자가 아닙니다.");
        }
    }

}
