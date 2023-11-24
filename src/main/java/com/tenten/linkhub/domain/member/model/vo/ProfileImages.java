package com.tenten.linkhub.domain.member.model.vo;

import com.tenten.linkhub.domain.member.model.ProfileImage;
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
public class ProfileImages {

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ProfileImage> profileImageList = new ArrayList<>();

    public void addProfileImage(ProfileImage profileImage) {
        validateNotNull(profileImage, "profileImage");
        this.profileImageList.add(profileImage);
    }

    public void changeProfileImage(ProfileImage profileImage) {
        validateNotNull(profileImage, "profileImages");

        List<ProfileImage> imageList = retrieveProfileImages();

        if (imageList.isEmpty()) {
            addProfileImage(profileImage);
            return;
        }

        retrieveProfileImages().get(0).deleteProfileImage();
        addProfileImage(profileImage);
    }

    public void removeProfileImage(ProfileImage profileImage) {
        this.profileImageList.remove(profileImage);
    }

    public List<ProfileImage> retrieveProfileImages() {
        return profileImageList.stream()
                .filter(profileImage -> !profileImage.getIsDeleted())
                .toList();
    }
}
