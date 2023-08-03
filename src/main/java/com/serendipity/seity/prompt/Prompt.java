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
    private boolean isExist;                // 공유된 이후에 삭제된 경우 false, 공유되지 않은채로 삭제되면 DB에서 삭제

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
        this.isExist = true;
    }

    public static Prompt createPrompt(String id, String userId, String llm, Qna qna) {

        List<Qna> qnas = new ArrayList<>();
        qnas.add(qna);
        return new Prompt(id, userId, llm, false, qnas);
    }

    public void addQna(Qna qna) {

        qnaList.add(qna);
    }

    public void addExtraAnswer(String answer) {

        qnaList.get(qnaList.size() - 1).addAnswer(answer);
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public void delete() {
        this.isExist = false;
    }
}
