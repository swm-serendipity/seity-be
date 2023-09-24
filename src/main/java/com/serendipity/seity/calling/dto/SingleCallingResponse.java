package com.serendipity.seity.calling.dto;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 단일 소명 요청을 조회할 때 사용하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SingleCallingResponse {

    private String callingId;
    private String detectionId;
    private String promptId;
    private String question;
    private String answer;

    public static SingleCallingResponse of(Calling calling, PromptDetection detection, Prompt prompt) {

        return new SingleCallingResponse(
                calling.getId(),
                detection.getId(),
                prompt.getId(),
                prompt.getQnaList().get(detection.getIndex()).getQuestion(),
                prompt.getQnaList().get(detection.getIndex()).getAnswer()
                );
    }
}
