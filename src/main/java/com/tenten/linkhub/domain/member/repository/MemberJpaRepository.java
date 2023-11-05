package com.tenten.linkhub.domain.member.repository;

import com.tenten.linkhub.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.profileImages pi WHERE m.id IN :memberIds AND m.isDeleted = false")
    List<Member> findMemberJoinProfileImageByMemberIds(List<Long> memberIds);

    boolean existsByNewsEmail(String email);

}
