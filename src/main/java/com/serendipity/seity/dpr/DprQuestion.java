package com.serendipity.seity.dpr;

import com.serendipity.seity.dpr.dto.DprDetectionResponse;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * dpr 질문을 저장하는 엔티티 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Document("DprQuestions")
@Getter
@NoArgsConstructor
public class DprQuestion {

    @Id
    private String id;

    private String question;
    private List<DprDetectionResponse> answers;

    private DprQuestion(String question, List<DprDetectionResponse> answers) {
        this.question = question;
        this.answers = answers;
    }

    public static DprQuestion createDprQuestion(String question, List<DprDetectionResponse> answers) {

        return new DprQuestion(question, answers);
    }
}
