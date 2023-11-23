package com.tenten.linkhub.domain.member.model;

import static com.tenten.linkhub.global.util.CommonValidator.validateMaxSize;
import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

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
import java.util.List;
import java.util.Optional;
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

    @Column(length = 24)
    private String nickname;

    @Column(length = 500)
    private String aboutMe;

    @Column(length = 320)
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

    public Member(String socialId, Provider provider, Role role, String nickname, String aboutMe, String newsEmail,
            Boolean isSubscribed) {
        this.socialId = socialId;
        this.provider = provider;
        this.role = role;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.newsEmail = newsEmail;
        this.isSubscribed = isSubscribed;
    }

    public void addProfileImage(ProfileImage profileImage) {
        this.profileImages.addProfileImage(profileImage);
        profileImage.changeMember(this);
    }

    public void addFavoriteCategory(FavoriteCategory favoriteCategory) {
        this.favoriteCategories.addFavoriteCategory(favoriteCategory);
        favoriteCategory.changeMember(this);
    }

    public List<ProfileImage> retrieveProfileImages() {
        return this.profileImages.retrieveProfileImages();
    }

    public List<FavoriteCategory> retrieveFavoriteCategories() {
        return this.favoriteCategories.retrieveFavoriteCategories();
    }

    public void deleteMember() {
        this.isDeleted = true;
    }

    public Member update(
            String nickname,
            String aboutMe,
            String newsEmail,
            Optional<ProfileImage> profileImage,
            FavoriteCategory favoriteCategory,
            Boolean isSubscribed
    ) {
        validateMaxSize(nickname, 24, "nickName");
        validateMaxSize(aboutMe, 500, "aboutMe");
        validateMaxSize(newsEmail, 300, "newsEmail");
        validateNotNull(isSubscribed, "isSubscribed");

        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.newsEmail = newsEmail;
        this.isSubscribed = isSubscribed;

        profileImage.ifPresent(this::changeProfileImage);
        changeFavoriteCategory(favoriteCategory);

        return this;
    }

    private void changeProfileImage(ProfileImage profileImage) {
        this.profileImages.changeProfileImage(profileImage);
        profileImage.changeMember(this);
    }

    private void changeFavoriteCategory(FavoriteCategory favoriteCategory) {
        this.favoriteCategories.changeFavoriteCategory(favoriteCategory);
        favoriteCategory.changeMember(this);
    }

}
