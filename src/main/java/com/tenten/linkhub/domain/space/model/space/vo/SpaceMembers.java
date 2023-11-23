package com.tenten.linkhub.domain.space.model.space.vo;

import com.tenten.linkhub.domain.space.model.space.Role;
import com.tenten.linkhub.domain.space.model.space.SpaceMember;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class SpaceMembers {

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<SpaceMember> spaceMemberList = new ArrayList<>();

    public void addSpaceMember(SpaceMember spaceMember) {
        validateNotNull(spaceMember, "spaceImages");

        this.spaceMemberList.add(spaceMember);
    }

    /**
     * Space와 SpaceMember간의 편의 메서드용 메서드.
     */
    public void removeSpaceMember(SpaceMember spaceMember) {
        this.spaceMemberList.remove(spaceMember);
    }

    public List<SpaceMember> getSpaceMemberList() {
        return spaceMemberList.stream()
                .filter(spaceMember -> !spaceMember.getIsDeleted())
                .collect(Collectors.toList());
    }

    public List<SpaceMember> getSortedSpaceMemberList() {
        return getSpaceMemberList()
                .stream()
                .sorted(Comparator.comparing(spaceMember -> spaceMember.getRole().ordinal()))
                .toList();
    }

    public List<Long> getSpaceMemberIds() {
        return getSpaceMemberList()
                .stream()
                .map(SpaceMember::getMemberId)
                .toList();
    }

    public void deleteAll() {
        spaceMemberList.forEach(SpaceMember::deleteSpaceMember);
    }

    public Boolean hasHigherRoleCanView(Long memberId) {
        Optional<SpaceMember> spaceMember = getSpaceMemberList()
                .stream()
                .filter(sm -> Objects.equals(sm.getMemberId(), memberId))
                .findFirst();

        if (spaceMember.isEmpty()) {
            return false;
        }

        return spaceMember.get().hasHigherRoleCanView();
    }

    public boolean containMember(Long memberId) {
        return spaceMemberList.
                stream()
                .anyMatch(m -> Objects.equals(m.getMemberId(), memberId));
    }

    public void changeSpaceMembersRole(Long targetMemberId, Role role) {
        SpaceMember targetSpaceMember = getSpaceMemberList()
                .stream()
                .filter(sm -> Objects.equals(sm.getMemberId(), targetMemberId))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("해당 스페이스멤버를 찾지 못했습니다."));

        targetSpaceMember.changeRole(role);
    }

}
