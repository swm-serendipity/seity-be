package com.serendipity.seity.detection;

import com.serendipity.seity.common.BaseTimeEntity;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 프롬프트에서 개인/중요 정보 탐지 시 사용되는 엔티티입니다.
 *
 * TODO: detection이 있는 경우 프롬프트 삭제 불가능하게
 * @author Min Ho CHO
 */
@Document("PromptDetections")
@Getter
@NoArgsConstructor
public class PromptDetection extends BaseTimeEntity {

    @Id
    private String id;
    private String promptId;
    private String userId;
    private String part;
    private boolean isDetected;
    private int index;
    List<DetectionWord> detectionWords;

    private PromptDetection(String promptId, String userId, String part, boolean isDetected, int index, List<DetectionWord> detectionWords) {
        this.promptId = promptId;
        this.userId = userId;
        this.part = part;
        this.isDetected = isDetected;
        this.index = index;
        this.detectionWords = detectionWords;
    }

    public static PromptDetection createPromptDetection(Prompt prompt, Member member, int index,
                                                        List<DetectionWord> detectionWords) {

        for (DetectionWord detectionWord : detectionWords) {
            if (detectionWord.getStatus() != DetectionStatus.DE_IDENTIFIED) {   // 비식별화가 해제된 것이 있다면
                return new PromptDetection(prompt.getId(), member.getId(), member.getPart().getValue(),
                        true, index, detectionWords);
            }
        }

        return new PromptDetection(prompt.getId(), member.getId(), member.getPart().getValue(),
                false, index, detectionWords);
    }

    /**
     * 1개의 탐지 내역에 대해 해결되었다고 갱신하는 메서드입니다.
     * @param index 탐지 내역 index
     */
    public void solveDetection(int index) {

        detectionWords.get(index).solve();
    }
}
