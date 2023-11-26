package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import com.tenten.linkhub.global.util.CommonValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Getter
@Table(name = "invitations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id", nullable = false)
    private Space space;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Boolean isAccepted;

    @Column(nullable = false)
    private Long notificationId;

    public Invitation(Space space, Role role, Long memberId, Long notificationId) {
        validateNotNull(space, "space");
        validateNotNull(role, "role");
        validateNotNull(memberId, "memberId");
        validateNotNull(notificationId, "notificationId");

        this.space = space;
        this.role = role;
        this.memberId = memberId;
        this.isAccepted = false;
        this.notificationId = notificationId;
    }

    public void changeIsAcceptedAsTrue(Long memberId) {
        validateOwnership(memberId);

        isAccepted = true;
    }

    public void validateOwnership(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new UnauthorizedAccessException("해당 멤버는 이 초대의 대상이 아닙니다.");
        }
    }

}
