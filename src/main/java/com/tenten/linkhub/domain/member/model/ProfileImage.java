package com.tenten.linkhub.domain.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Getter
@Table(name = "profile_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {
    private static final int MAX_PATH_LENGTH = 2083;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "path", nullable = false)
    private String path;

    public ProfileImage(Long id, Member member, String path) {
        this.id = id;
        this.member = member;
        this.path = path;
    }

    public void changeMember(Member member) {
        if (this.member != null) {
            this.member.getProfileImages().remove(this);
        }

        this.member = member;
    }

}
