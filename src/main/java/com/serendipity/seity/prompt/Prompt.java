package com.serendipity.seity.prompt;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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
public class Prompt {

    @Id
    private String id;
    private String userId;
    private String llm;
    private boolean isFavorite;
    private List<Qna> qnaList;

    private LocalDateTime createTime;       // @Async 애노테이션을 사용할 경우 Auditing 적용 안되는 이슈

    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    private Prompt(String id, String userId, String llm, boolean isFavorite, List<Qna> qnaList) {
        this.id = id;
        this.userId = userId;
        this.llm = llm;
        this.isFavorite = isFavorite;
        this.qnaList = qnaList;
        this.createTime = LocalDateTime.now();
    }

    public static Prompt createPrompt(String id, String userId, String llm, boolean isFavorite, Qna qna) {

        List<Qna> qnas = new ArrayList<>();
        qnas.add(qna);
        return new Prompt(id, userId, llm, isFavorite, qnas);
    }

    public void addQna(Qna qna) {

        qnaList.add(qna);
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

}
