package com.tenten.linkhub.domain.space.model.space;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Entity
@Table(name = "scraps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sourceSpaceId;

    @Column(nullable = false)
    private Long targetSpaceId;

    @Column(nullable = false)
    private Long memberId;

    public Scrap(Long sourceSpaceId, Long targetSpaceId, Long memberId) {
        validateNotNull(sourceSpaceId, "spaceId");
        validateNotNull(targetSpaceId, "targetSpaceId");
        validateNotNull(memberId, "memberId");

        this.sourceSpaceId = sourceSpaceId;
        this.targetSpaceId = targetSpaceId;
        this.memberId = memberId;
    }

}
