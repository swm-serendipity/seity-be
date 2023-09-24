package com.serendipity.seity.calling.dto;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.serendipity.seity.calling.CallingStatus.READ;

/**
 * 복수개의 소명을 조회할 때(알림 창) 사용하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleCallingResponse {

    private String callingId;
    private String senderProfileBackgroundHex;
    private String senderProfileTextHex;
    private String senderRole;
    private String question;
    private boolean isRead;

    public static MultipleCallingResponse of(Calling calling, PromptDetection detection, Prompt prompt, Member sender) {

        boolean isRead = calling.getStatus().equals(READ);

        return new MultipleCallingResponse(
                calling.getId(),
                sender.getProfileBackgroundHex(),
                sender.getProfileTextHex(),
                "보안관리팀",
                prompt.getQnaList().get(detection.getIndex()).getQuestion(),
                isRead
                );
    }
}
