package com.serendipity.seity.detection;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 검출된 단어를 표현하는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetectionWord {

    private int index;
    private int length;
    @Enumerated(EnumType.STRING)
    private DetectionStatus status;
    @Enumerated(EnumType.STRING)
    private DetectionInfo detectionInfo;

    public void solve() {

        status = DetectionStatus.SOLVED;
    }

    public static DetectionWord createDetectionWord(int index, int length, DetectionStatus status, DetectionInfo detectionInfo) {

        return new DetectionWord(index, length, status, detectionInfo);
    }
}
