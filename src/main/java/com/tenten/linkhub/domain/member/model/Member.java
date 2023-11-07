package com.tenten.linkhub.domain.member.model;

import com.tenten.linkhub.domain.member.model.vo.FavoriteCategories;
import com.tenten.linkhub.domain.member.model.vo.ProfileImages;
import com.tenten.linkhub.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
    private ProfileImages profileImages = new ProfileImages();

    @Embedded
    private FavoriteCategories favoriteCategories = new FavoriteCategories();

    @Column
    private String nickname;

    @Column
    private String aboutMe;

    @Column
    private String newsEmail;

    @Column
    private Boolean isSubscribed;

    public Member(String socialId, Provider provider, Role role, String nickname, String aboutMe, String newsEmail,
            Boolean isSubscribed, ProfileImage profileImage, FavoriteCategory favoriteCategory) {
        this.socialId = socialId;
        this.provider = provider;
        this.role = role;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.newsEmail = newsEmail;
        this.isSubscribed = isSubscribed;
        this.addProfileImage(profileImage);
        this.addFavoriteCategory(favoriteCategory);
    }

    public Member(String socialId, Provider provider, Role role) {
        this.socialId = socialId;
        this.provider = provider;
        this.role = role;
    }

    public void addProfileImage(ProfileImage profileImage) {
        this.profileImages.addProfileImage(profileImage);
        profileImage.changeMember(this);
    }

    public void addFavoriteCategory(FavoriteCategory favoriteCategory) {
        this.favoriteCategories.addFavoriteCategory(favoriteCategory);
        favoriteCategory.changeMember(this);
    }

    public ProfileImage getProfileImage() {
        return this.profileImages.getProfileImage();
    }

}
