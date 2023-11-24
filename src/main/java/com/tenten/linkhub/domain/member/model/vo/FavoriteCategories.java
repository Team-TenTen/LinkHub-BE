package com.tenten.linkhub.domain.member.model.vo;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

import com.tenten.linkhub.domain.member.model.FavoriteCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class FavoriteCategories {

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
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

    public void changeFavoriteCategory(FavoriteCategory favoriteCategory) {
        validateNotNull(favoriteCategory, "favoriteCategories");

        List<FavoriteCategory> categoryList = getFavoriteCategoryList();

        if (categoryList.isEmpty()) {
            addFavoriteCategory(favoriteCategory);
            return;
        }

        removeFavoriteCategory();
        addFavoriteCategory(favoriteCategory);
    }

    private void removeFavoriteCategory() {
        this.favoriteCategoryList.remove(0);
    }
}
