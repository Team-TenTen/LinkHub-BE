package com.tenten.linkhub.domain.space.model.space;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Table(name = "space_member", uniqueConstraints = {
        @UniqueConstraint(
                name = "space_member_unique",
                columnNames = {
                        "space_id",
                        "member_id"
                }
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id", nullable = false)
    private Space space;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public SpaceMember(Long memberId, Role role) {
        validateNotNull(memberId, "memberId");
        validateNotNull(role, "role");

        this.memberId = memberId;
        this.role = role;
    }

    public void changeSpace(Space space) {
        if (this.space != null) {
            this.space.removeSpaceMember(this);
        }

        this.space = space;
    }

}
