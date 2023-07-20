package com.serendipity.seity.prompt.repository;

import com.serendipity.seity.prompt.Prompt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * prompt 저장을 위한 repository 입니다.
 *
 * @author Min Ho CHO
 */
@Repository
public interface PromptRepository extends MongoRepository<Prompt, String> {

}
