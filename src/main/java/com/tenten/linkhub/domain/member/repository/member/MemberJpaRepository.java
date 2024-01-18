package com.tenten.linkhub.domain.member.repository.member;

import com.tenten.linkhub.domain.member.model.Member;
import com.tenten.linkhub.domain.member.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.profileImages pi WHERE m.id IN :memberIds AND m.isDeleted = false")
    List<Member> findMemberJoinProfileImageByMemberIds(List<Long> memberIds);

    boolean existsByNewsEmailAndIsDeletedFalse(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Member m " +
            "WHERE m.socialId = :socialId " +
            "AND m.provider = :provider " +
            "AND m.isDeleted = false")
    Optional<Member> findBySocialIdAndProvider(String socialId, Provider provider);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.profileImages pi " +
            "LEFT JOIN FETCH m.favoriteCategories fc " +
            "WHERE m.id = :memberId AND m.isDeleted = false")
    Optional<Member> findByIdWithImageAndCategory(Long memberId);

    @Query("SELECT m FROM Member m WHERE m.id = :memberId AND m.isDeleted = false")
    Optional<Member> findExistingMemberById(Long memberId);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.profileImages pi " +
            "LEFT JOIN FETCH m.favoriteCategories fc " +
            "WHERE m.id IN :memberIds AND m.isDeleted = false ")
    List<Member> findMembersWithProfileImageAndCategoryByMemberIds(List<Long> memberIds);

    @Query("SELECT m.id FROM Member m WHERE m.newsEmail = :email")
    Long findMemberIdByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.id IN :memberIds AND m.isDeleted = false")
    List<Member> findByIds(List<Long> memberIds);
}

