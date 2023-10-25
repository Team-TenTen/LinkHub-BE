package com.tenten.linkhub.domain.notification.model;

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

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_checked")
    private boolean isChecked;

    @Builder
    public Notification(Long id, Long recipientId, Long senderId, NotificationType notificationType, String content,
            boolean isChecked) {
        this.id = id;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.notificationType = notificationType;
        this.content = content;
        this.isChecked = false;
    }
}
