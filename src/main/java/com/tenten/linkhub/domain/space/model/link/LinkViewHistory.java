package com.tenten.linkhub.domain.space.model.link;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "link_view_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LinkViewHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    private LinkViewHistory(Long memberId, Link link) {
        this.memberId = memberId;
        this.link = link;
    }

    public static LinkViewHistory toLinkViewHistory(Long memberId, Link link) {
        return new LinkViewHistory(memberId, link);
    }
}
