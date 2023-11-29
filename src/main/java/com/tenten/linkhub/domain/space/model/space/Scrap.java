package com.tenten.linkhub.domain.space.model.space;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Long spaceId;

    @Column(nullable = false)
    private Long memberId;

    public Scrap(Long spaceId, Long memberId) {
        validateNotNull(spaceId, "spaceId");
        validateNotNull(memberId, "memberId");

        this.spaceId = spaceId;
        this.memberId = memberId;
    }

}
