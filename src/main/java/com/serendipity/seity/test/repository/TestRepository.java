package com.serendipity.seity.test.repository;

import com.serendipity.seity.test.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Test 엔티티 repository입니다.
 *
 * @author Min Ho CHO
 */
public interface TestRepository extends JpaRepository<TestEntity, Long> {

}
