package com.tenten.linkhub.domain.member.model;

import com.tenten.linkhub.domain.member.model.vo.AboutMe;
import com.tenten.linkhub.domain.member.model.vo.Nickname;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "oauth_email", nullable = false)
    private String OAuthEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Embedded
    @Column(name = "nickname", nullable = false)
    private Nickname nickname;

    @Embedded
    @Column(name = "about_me")
    private AboutMe aboutMe;
    
    @Column(name = "news_email")
    private String newsEmail;

    @Column(name = "is_email_verified")
    private boolean isEmailVerified;

    @OneToMany(mappedBy = "member")
    private Set<ProfileImage> profileImages = new HashSet<>();

    @OneToMany(mappedBy = "following")
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<FavoriteCategory> favoriteCategories = new HashSet<>();

    @Builder
    public Member(Long id, String OAuthEmail, Role role, String newsEmail, Nickname nickname, AboutMe aboutMe,
                  boolean isEmailVerified) {
        this.id = id;
        this.OAuthEmail = OAuthEmail;
        this.role = role;
        this.newsEmail = newsEmail;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.isEmailVerified = false;
    }

}
