package com.tenten.linkhub.domain.member.model;

import static com.tenten.linkhub.global.util.CommonValidator.validateMaxSize;

import com.tenten.linkhub.global.entity.BaseEntity;
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
@Getter
@Table(name = "profile_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends BaseEntity {
    private static final int MAX_PATH_LENGTH = 2083;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @Column(length = MAX_PATH_LENGTH, nullable = false)
    private String path;

    @Column(nullable = false)
    private String name;

    public ProfileImage(String path, String name) {
        validateMaxSize(path, MAX_PATH_LENGTH, "path");
        validateMaxSize(name, 255, "name");

        this.path = path;
        this.name = name;
    }

    public void changeMember(Member member) {
        if (this.member != null) {
            this.member.getProfileImages().removeProfileImage(this);
        }

        this.member = member;
    }

    public Long deleteProfileImage() {
        this.isDeleted = true;
        return id;
    }

}
