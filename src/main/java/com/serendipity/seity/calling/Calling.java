package com.serendipity.seity.calling;

import com.serendipity.seity.common.BaseTimeEntity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.serendipity.seity.calling.CallingStatus.*;

/**
 * 소명 요청과 소명을 나타내는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Document("CallingRequests")
@Getter
@NoArgsConstructor
public class Calling extends BaseTimeEntity {

    @Id
    private String id;
    private String promptDetectionId;
    private String senderId;
    private String receiverId;
    private CallingStatus status;
    private String content;

    private Calling(String promptDetectionId, String senderId, String receiverId, CallingStatus status, String content) {
        this.promptDetectionId = promptDetectionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.content = content;
    }

    public static Calling createCalling(String promptDetectionId, String senderId, String receiverId) {

        return new Calling(promptDetectionId, senderId, receiverId, PENDING, "");
    }

    public void sendCalling(String content) {

        this.content = content;
        status = CALLING_SENT;
    }

    public void solveCalling() {

        this.status = SOLVED;
    }
}
