package com.serendipity.seity.calling.dto.calling;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.detection.dto.SingleDetectionResponse;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 단일 소명 이력을 조회하는데 사용하는 클래스입니다.
 * TODO: 마감기한 설정 (현재는 소명 요청 이후 7일로 설정)
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SingleCallingResponse {

    private String callingId;
    private String llm;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private String userId;
    private String userName;
    private String userProfileBackgroundHex;
    private String userProfileTextHex;
    private SingleDetectionResponse detection;
    private String status;
    private String content;

    public static SingleCallingResponse of(Calling calling, Member member, Prompt prompt, PromptDetection detection) {

        return new SingleCallingResponse(
                calling.getId(),
                prompt.getLlm(),
                calling.getCreateTime(),
                calling.getCreateTime().plusDays(7),
                member.getId(),
                member.getName(),
                member.getProfileBackgroundHex(),
                member.getProfileTextHex(),
                SingleDetectionResponse.of(prompt, member, detection),
                calling.getStatus().getValue(),
                calling.getContent()
        );
    }
}
