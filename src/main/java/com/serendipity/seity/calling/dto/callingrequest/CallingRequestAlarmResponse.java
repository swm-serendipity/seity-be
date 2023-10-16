package com.serendipity.seity.calling.dto.callingrequest;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 직원에게 소명 요청 알림이 전송될 때 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CallingRequestAlarmResponse {

    private String callingId;
    private String promptId;
    private String question;
    private String answer;
    private String senderProfileBackgroundHex;
    private String senderProfileTextHex;
    private String senderName;
    private String senderRole;

    public static CallingRequestAlarmResponse of(Calling calling, Prompt prompt, int index, Member sender) {

        String senderRole = "보안관리팀";

        for (String role: sender.getRoles()) {

            if (role.equals("ADMIN")) {
                // TODO: 보안관리팀의 이름이 구체화 되면 if문 구체화
            }
        }

        return new CallingRequestAlarmResponse(
                calling.getId(),
                prompt.getId(),
                prompt.getQnaList().get(index).getQuestion(),
                prompt.getQnaList().get(index).getAnswer(),
                sender.getProfileBackgroundHex(),
                sender.getProfileTextHex(),
                sender.getName(),
                senderRole
                );
    }
}
