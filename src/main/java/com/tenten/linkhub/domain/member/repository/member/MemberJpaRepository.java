package com.tenten.linkhub.domain.member.repository.member;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.profileImages pi WHERE m.id IN :memberIds AND m.isDeleted = false")
    List<Member> findMemberJoinProfileImageByMemberIds(List<Long> memberIds);

    boolean existsByNewsEmail(String email);

    Optional<Member> findBySocialIdAndProvider(String socialId, Provider provider);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.profileImages pi " +
            "LEFT JOIN FETCH m.favoriteCategories fc " +
            "WHERE m.id = :memberId AND m.isDeleted = false")
    Optional<Member> findByIdWithImageAndCategory(Long memberId);

}

