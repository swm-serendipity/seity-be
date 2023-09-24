package com.serendipity.seity.calling.repository;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.calling.CallingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 소명 엔티티에 대한 repository 클래스입니다.
 *
 * @author Min Ho CHO
 */
public interface CallingRepository extends MongoRepository<Calling, String> {

    List<Calling> findByReceiverId(String receiverId, Pageable pageable);
    int countByReceiverId(String receiverId);
    List<Calling> findByReceiverIdAndStatus(String receiverId, CallingStatus status);
}
