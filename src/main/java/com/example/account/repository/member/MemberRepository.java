package com.example.account.repository.member;

import com.example.account.domain.member.Member;
import com.example.account.dto.account.CreateAccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNameAndBirthDayAndPhoneNumber(String name, String birthDay, String phoneNumber);

    @Query("select m " +
            "from Member m " +
            "left join fetch Account a " +
            "on m.id = a.memberId " +
            "and a.accountStatus = 'IN_USE' " +
            "where m.id = :memberId ")
    Optional<Member> findAccountListByMemberId(@Param("memberId") Long memberId);

}
