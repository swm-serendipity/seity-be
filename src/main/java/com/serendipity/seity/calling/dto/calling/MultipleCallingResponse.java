package com.serendipity.seity.calling.dto.calling;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.detection.DetectionWord;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.serendipity.seity.calling.CallingStatus.SOLVED;

/**
 * 복수 개의 소명 리스트를 조회할 때 사용하는 클래스입니다.
 *
 * @author Min Ho Ho
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleCallingResponse {

    private String callingId;
    private String userId;
    private String userName;
    private String userProfileBackgroundHex;
    private String userProfileTextHex;
    private String llm;
    private List<String> detectionDivision;
    private boolean isResolved;
    private LocalDateTime lastModifiedAt;

    public static MultipleCallingResponse of(Calling calling, PromptDetection detection, Member member, Prompt prompt) {

        Set<String> detections = new HashSet<>();

        for (DetectionWord word : detection.getDetectionWords()) {
            detections.add(word.getDetectionInfo().getValue());
        }

        return new MultipleCallingResponse(
                calling.getId(),
                member.getId(),
                member.getName(),
                member.getProfileBackgroundHex(),
                member.getProfileTextHex(),
                prompt.getLlm(),
                new ArrayList<>(detections),
                calling.getStatus().equals(SOLVED),
                calling.getLastModifiedTime()
        );
    }
}
