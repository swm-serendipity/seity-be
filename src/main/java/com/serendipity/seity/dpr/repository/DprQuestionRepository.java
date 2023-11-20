package com.serendipity.seity.dpr.repository;

import com.serendipity.seity.dpr.DprQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * dpr question repository 클래스입니다.
 *
 * @author Min Ho CHO
 */
public interface DprQuestionRepository extends MongoRepository<DprQuestion, String> {


}
