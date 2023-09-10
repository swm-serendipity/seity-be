package com.serendipity.seity.detection.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.detection.DetectionStatus;
import com.serendipity.seity.detection.DetectionWord;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 1개의 민감정보 탐지 내용을 조회할 때 사용하는 response 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SingleDetectionResponse {

    private String id;
    private String userId;
    private String userName;
    private String profileBackgroundHex;
    private String profileTextHex;
    private String promptId;
    private String question;
    private List<DetectionDto> detectionList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static SingleDetectionResponse of(Prompt prompt, Member member, PromptDetection promptDetection) {

        List<DetectionDto> detectionDtos = new ArrayList<>();
        for (DetectionWord word : promptDetection.getDetectionWords()) {

            if (word.getStatus() == DetectionStatus.DE_IDENTIFIED) {
                detectionDtos.add(new DetectionDto(word.getIndex(), word.getLength(), true, word.getDetectionInfo().getValue()));
            } else {
                detectionDtos.add(new DetectionDto(word.getIndex(), word.getLength(), false, word.getDetectionInfo().getValue()));
            }
        }

        return new SingleDetectionResponse(
                promptDetection.getId(),
                member.getId(),
                member.getName(),
                member.getProfileBackgroundHex(),
                member.getProfileTextHex(),
                prompt.getId(),
                prompt.getQnaList().get(promptDetection.getIndex()).getQuestion(),
                detectionDtos,
                promptDetection.getCreateTime()
        );
    }
}
