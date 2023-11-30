package com.tenten.linkhub.domain.space.repository.invitation;

import com.tenten.linkhub.domain.space.model.space.Invitation;
import com.tenten.linkhub.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DefaultInvitationRepository implements InvitationRepository {

    private final InvitationJpaRepository invitationJpaRepository;

    @Override
    public Invitation getByNotificationId(Long notificationId) {
        return invitationJpaRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new DataNotFoundException("해당 notificationId에 해당하는 Invitation를 찾을수 없습니다."));
    }

    @Override
    public Invitation save(Invitation invitation) {
        return invitationJpaRepository.save(invitation);
    }

    @Override
    public void deleteByNotificationId(Long notificationId, Long memberId) {
        invitationJpaRepository.deleteByNotificationId(notificationId, memberId);
    }
}
