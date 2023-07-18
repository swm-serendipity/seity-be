package com.serendipity.seity.prompt;

import com.serendipity.seity.common.BaseTimeEntity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 프롬프트 엔티티입니다.
 *
 * @author Min Ho CHO
 */
@Document("prompts")
@Getter
@NoArgsConstructor
public class Prompt extends BaseTimeEntity {

    @Id
    private String id;
    private String userId;
    private String llm;
    private boolean isFavorite;
    private List<Qna> qnaList;

    private Prompt(String userId, String llm, boolean isFavorite, List<Qna> qnaList) {
        this.userId = userId;
        this.llm = llm;
        this.isFavorite = isFavorite;
        this.qnaList = qnaList;
    }

    public static Prompt createPrompt(String userId, String llm, boolean isFavorite, Qna qna) {

        List<Qna> qnas = new ArrayList<>();
        qnas.add(qna);
        return new Prompt(userId, llm, isFavorite, qnas);
    }

    public void addQna(Qna qna) {

        qnaList.add(qna);
    }

}
