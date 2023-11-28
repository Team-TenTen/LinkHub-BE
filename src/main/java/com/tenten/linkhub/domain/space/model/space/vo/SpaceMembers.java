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
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tenten.linkhub.domain.space.model.space.Role.CAN_EDIT;
import static com.tenten.linkhub.domain.space.model.space.Role.OWNER;
import static com.tenten.linkhub.global.util.CommonValidator.validateNotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class SpaceMembers {

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpaceMember> spaceMemberList = new ArrayList<>();

    public void addSpaceMember(SpaceMember spaceMember) {
        validateNotNull(spaceMember, "spaceImages");
        validateDuplicationSpaceMember(spaceMember.getMemberId());

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
        return spaceMemberList
                .stream()
                .anyMatch(sm -> Objects.equals(sm.getMemberId(), memberId) && !sm.getIsDeleted());
    }

    public void changeSpaceMembersRole(Long targetMemberId, Role role) {
        SpaceMember targetSpaceMember = getSpaceMemberList()
                .stream()
                .filter(sm -> Objects.equals(sm.getMemberId(), targetMemberId))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("해당 스페이스멤버를 찾지 못했습니다."));

        if (Objects.equals(role, OWNER)) {
            SpaceMember spaceOwner = getSpaceOwner();
            spaceOwner.changeRole(CAN_EDIT);
        }

        targetSpaceMember.changeRole(role);
    }

    public void deleteSpaceMember(Long memberId) {
        SpaceMember spaceMember = getSpaceMemberList()
                .stream()
                .filter(sm -> Objects.equals(sm.getMemberId(), memberId))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("해당하는 스페이스 멤버가 존재하지 않습니다."));

        if (Objects.equals(spaceMember.getRole(), OWNER)) {
            throw new IllegalStateException("스페이스의 주인은 스페이스를 나갈 수 없습니다. 나가기 대신 스페이스 삭제를 해주세요.");
        }

        spaceMember.deleteSpaceMember();
    }

    public void validateDuplicationSpaceMember(Long memberId) {
        if (getSpaceMemberIds().contains(memberId)) {
            throw new DuplicateKeyException("해당 멤버는 이미 스페이스의 멤버입니다.");
        }
    }

    private SpaceMember getSpaceOwner() {
        return getSpaceMemberList()
                .stream()
                .filter(sm -> Objects.equals(sm.getRole(), OWNER))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("해당 스페이스에 Owner가 존재하지 않습니디."));
    }

}
