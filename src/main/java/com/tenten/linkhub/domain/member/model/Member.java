package com.tenten.linkhub.domain.member.model;

import com.tenten.linkhub.domain.member.model.vo.AboutMe;
import com.tenten.linkhub.domain.member.model.vo.NewsEmail;
import com.tenten.linkhub.domain.member.model.vo.Nickname;
import com.tenten.linkhub.domain.space.model.space.SpaceImage;
import com.tenten.linkhub.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String OAuthEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String aboutMe;

    @Column
    private String newsEmail;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ProfileImage> profileImages = new ArrayList<>();

    public Member(String OAuthEmail, Role role, String nickname, String aboutMe, String newsEmail, boolean isEmailVerified) {
        this.OAuthEmail = OAuthEmail;
        this.role = role;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.newsEmail = newsEmail;
        this.isEmailVerified = isEmailVerified;
    }

    public void addProfileImage(ProfileImage profileImage) {
        this.profileImages.add(profileImage);
        profileImage.changeMember(this);
    }

}
