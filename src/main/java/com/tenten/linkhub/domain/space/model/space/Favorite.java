package com.tenten.linkhub.domain.space.model.space;

import com.tenten.linkhub.global.entity.BaseTimeEntity;
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

    @Column(name = "space_id", nullable = false)
    private Long spaceId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    public Favorite(Long spaceId, Long memberId) {
        this.spaceId = spaceId;
        this.memberId = memberId;
    }

}
