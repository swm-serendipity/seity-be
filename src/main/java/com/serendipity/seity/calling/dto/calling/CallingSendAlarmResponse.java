package com.serendipity.seity.calling.dto.calling;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소명 전송 시 전송되는 알람에 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CallingSendAlarmResponse {

    private String callingId;
    private String promptId;
    private String question;
    private String answer;
    private String senderProfileBackgroundHex;
    private String senderProfileTextHex;
    private String senderName;
    private String senderRole;
    private String content;

    public static CallingSendAlarmResponse of(Calling calling, Prompt prompt, int index, Member sender) {

        return new CallingSendAlarmResponse(
                calling.getId(),
                prompt.getId(),
                prompt.getQnaList().get(index).getQuestion(),
                prompt.getQnaList().get(index).getAnswer(),
                sender.getProfileBackgroundHex(),
                sender.getProfileTextHex(),
                sender.getName(),
                sender.getPart().getValue(),
                calling.getContent()
        );
    }
}
