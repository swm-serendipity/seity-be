package com.serendipity.seity.dpr.repository;

import com.serendipity.seity.dpr.WikiWord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * wiki word repository 클래스입니다.
 *
 * @author Min Ho CHO
 */
public interface WikiWordRepository extends MongoRepository<WikiWord, String> {

    boolean existsByWord(String word);
}
