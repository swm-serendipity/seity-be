package com.serendipity.seity.detection.repository;

import com.serendipity.seity.detection.PromptDetection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * PromptDetection repository 입니다.
 *
 * @author Min Ho CHO
 */
public interface PromptDetectionRepository extends MongoRepository<PromptDetection, String> {

    List<PromptDetection> findPromptDetectionByIsDetectedAndPartOrderByCreateTimeDesc(boolean isDetected, String part);
    List<PromptDetection> findPromptDetectionByIsDetectedAndPartOrderByCreateTimeDesc(boolean isDetected, String part, Pageable pageable);
}
