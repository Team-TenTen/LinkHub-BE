package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.global.entity.BaseTimeEntity;
import com.tenten.linkhub.global.exception.UnauthorizedAccessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.Objects;

@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(
                name = "space_member_unique",
                columnNames = {
                        "member_id",
                        "space_id"
                }
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id", nullable = false)
    private Space space;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    public Favorite(Space space, Long memberId) {
        this.space = space;
        this.memberId = memberId;
    }

    public void validateOwnership(Long memberId){
        if (!Objects.equals(this.memberId, memberId)) {
            throw new UnauthorizedAccessException("해당 멤버는 이 Favorite의 owner가 아닙니다.");
        }
    }

}
