package com.tenten.linkhub.domain.member.model;

import com.tenten.linkhub.domain.space.model.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "favorite_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public FavoriteCategory(Category category) {
        this.category = category;
    }

    public void changeMember(Member member) {
        if (this.member != null) {
            this.member.getFavoriteCategories().removeFavoriteCategory(this);
        }

        this.member = member;
    }
}
