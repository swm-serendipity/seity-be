package com.serendipity.seity.prompt;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프롬프트에서의 각 질문을 나타내는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Qna {

    private String question;
    private String answer;
    private int tokenNumber;

    public Qna(String question, String answer) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);

        this.question = question;
        this.answer = answer;
        this.tokenNumber = enc.encode(question).size() + enc.encode(answer).size();
    }

    public void addAnswer(String answer) {

        this.answer += answer;
    }
}
