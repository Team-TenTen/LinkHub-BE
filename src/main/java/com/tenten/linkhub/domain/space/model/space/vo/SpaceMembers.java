package com.tenten.linkhub.domain.space.model.space.vo;

import com.tenten.linkhub.domain.space.model.space.SpaceMember;
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
public class SpaceMembers {

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<SpaceMember> spaceMemberList = new ArrayList<>();

    public void addSpaceMember(SpaceMember spaceMember) {
        validateNotNull(spaceMember, "spaceImage");

        this.spaceMemberList.add(spaceMember);
    }

    public void removeSpaceMember(SpaceMember spaceMember){
        this.spaceMemberList.remove(spaceMember);
    }

}
