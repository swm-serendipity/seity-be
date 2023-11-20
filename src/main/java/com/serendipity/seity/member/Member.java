package com.serendipity.seity.member;

import com.serendipity.seity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * member 도메인 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberPart part;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false)
    private String profileBackgroundHex;

    @Column(nullable = false)
    private String profileTextHex;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != MemberStatus.DELETE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != MemberStatus.STOP;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status != MemberStatus.STOP && status != MemberStatus.DELETE;
    }

    @Override
    public boolean isEnabled() {
        return status != MemberStatus.WAITING_APPROVE && status != MemberStatus.STOP && status != MemberStatus.DELETE;
    }

    private Member(String password, String name, String loginId, String email, LocalDate birthDate, MemberPart part,
                   MemberStatus status, MemberRole role) {

        this.password = password;
        this.name = name;
        this.loginId = loginId;
        this.email = email;
        this.birthDate = birthDate;
        this.part = part;
        this.status = status;
        this.roles.add(role.getCode());

        MemberProfileColor color = MemberProfileColor.getRandomColor();
        this.profileBackgroundHex = color.getBackgroundHex();
        this.profileTextHex = color.getTextHex();
    }

    /**
     * member 엔티티를 생성하는 메서드입니다.
     * member 엔티티를 해당 메서드로만 생성됩니다.
     * @param password 비밀번호
     * @param name 이름
     * @param loginId 로그인 아이디
     * @param email 이메일
     * @param birthDate 생년월일
     * @param part 직무
     * @param role 직책
     * @return 생성된 member entity
     */
    public static Member createMember(String password, String name, String loginId, String email,
                                      LocalDate birthDate, MemberPart part, MemberRole role) {

        return new Member(password, name, loginId, email, birthDate, part, MemberStatus.WAITING_APPROVE, role);
    }

    /**
     * 사용자 정보 수정 메서드입니다.
     * @param name 이름
     * @param part 직무
     * @param role 권한
     */
    public void update(String name, MemberPart part, MemberRole role) {

        if (name != null) {

            this.name = name;
        }

        if (part != null) {

            this.part = part;
        }

        if (role != null) {

            roles.clear();
            roles.add(role.getCode());
        }
    }
}
