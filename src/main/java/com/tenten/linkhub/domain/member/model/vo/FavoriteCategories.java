package com.tenten.linkhub.domain.member.model.vo;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class FavoriteCategories {

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<FavoriteCategory> favoriteCategoryList = new ArrayList<>();

    public void addFavoriteCategory(FavoriteCategory favoriteCategory) {
        validateNotNull(favoriteCategory, "favoriteCategory");
        this.favoriteCategoryList.add(favoriteCategory);
    }

    public void removeFavoriteCategory(FavoriteCategory favoriteCategory) {
        this.favoriteCategoryList.remove(favoriteCategory);
    }

    public List<FavoriteCategory> retrieveFavoriteCategories() {
        return this.favoriteCategoryList;
    }
}
