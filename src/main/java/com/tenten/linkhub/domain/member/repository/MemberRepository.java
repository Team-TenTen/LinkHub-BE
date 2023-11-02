package com.tenten.linkhub.domain.member.repository;

public interface MemberRepository {

    boolean existsMemberByNewsEmail(String email);

}
