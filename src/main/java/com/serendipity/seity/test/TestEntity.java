package com.serendipity.seity.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 테스트를 위한 엔티티입니다.
 *
 * @author Min Ho CHO
 */
@Entity
@Getter
@NoArgsConstructor
public class TestEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private TestEntity(String name) {
        this.name = name;
    }

    public static TestEntity createTestEntity() {
        return new TestEntity("test");
    }
}
