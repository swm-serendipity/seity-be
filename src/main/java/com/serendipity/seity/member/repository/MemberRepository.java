package com.serendipity.seity.member.repository;

import com.serendipity.seity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * member jpa repository 입니다.
 *
 * @author Min Ho CHO
 */
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByLoginId(String loginId);
}
