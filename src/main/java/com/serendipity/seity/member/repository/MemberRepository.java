package com.serendipity.seity.member.repository;

import com.serendipity.seity.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * member jpa repository 입니다.
 *
 * @author Min Ho CHO
 */
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByEmail(String email);
    List<Member> findByRolesContaining(String role);
    List<Member> findByRolesContaining(String role, Pageable pageable);
    int countByRolesContaining(String role);
}
