package com.serendipity.seity.forbiddenword;

import com.serendipity.seity.common.BaseTimeEntity;
import com.serendipity.seity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 금칙어를 나타내는 클래스입니다.
 *
 * @author Min Ho HO
 */
@Entity
@Getter
@NoArgsConstructor
public class ForbiddenWord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "user_id")
    private String userId;

    private String value;

    private ForbiddenWord(String userId, String value) {
        this.userId = userId;
        this.value = value;
    }

    public static ForbiddenWord createForbiddenWord(Member member, String value) {

        return new ForbiddenWord(member.getId(), value);
    }
}
