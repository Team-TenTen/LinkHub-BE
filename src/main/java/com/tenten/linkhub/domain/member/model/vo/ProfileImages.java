package com.tenten.linkhub.domain.member.model.vo;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

import com.tenten.linkhub.domain.member.model.ProfileImage;
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
public class ProfileImages {

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ProfileImage> profileImageList = new ArrayList<>();

    public void addProfileImage(ProfileImage profileImage) {
        validateNotNull(profileImage, "profileImage");
        this.profileImageList.add(profileImage);
    }

    public void removeProfileImage(ProfileImage profileImage) {
        this.profileImageList.remove(profileImage);
    }

    public ProfileImage getProfileImage() {
        return this.profileImageList.get(0);
    }
}
