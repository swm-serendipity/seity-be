package com.serendipity.seity.detection.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 여러 개의 민감정보 탐지 내역을 조회할 때 사용하는 resposne 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleDetectionResponse {

    private String id;
    private String userId;
    private String userName;
    private String profileBackgroundHex;
    private String profileTextHex;
    private String question;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static MultipleDetectionResponse of(Optional<Prompt> prompt, Optional<Member> member, PromptDetection promptDetection) {

        // TODO: 삭제된 사용자이거나 삭제된 프롬프트일 경우 예외처리 필요

        String memberId = null;
        String memberName = null;
        String memberProfileBackgroundHex = null;
        String memberProfileTextHex = null;
        String question = null;

        if (prompt.isPresent()) {
            question = prompt.get().getQnaList().get(promptDetection.getIndex()).getQuestion();
        }
        if (member.isPresent()) {
            memberId = member.get().getId();
            memberName = member.get().getName();
            memberProfileBackgroundHex = member.get().getProfileBackgroundHex();
            memberProfileTextHex = member.get().getProfileTextHex();
        }

        return new MultipleDetectionResponse(
                promptDetection.getId(),
                memberId,
                memberName,
                memberProfileBackgroundHex,
                memberProfileTextHex,
                question,
                promptDetection.getCreateTime());
    }
}
