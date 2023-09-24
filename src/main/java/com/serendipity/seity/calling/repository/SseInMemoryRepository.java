package com.serendipity.seity.calling.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SseInMemoryRepository {

    private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public void put(String key, SseEmitter sseEmitter) {
        sseEmitterMap.put(key, sseEmitter);
    }

    public Optional<SseEmitter> get(String key) {
        return Optional.ofNullable(sseEmitterMap.get(key));
    }

    public List<SseEmitter> getListByKeyPrefix(String keyPrefix){
        return sseEmitterMap.keySet().stream()
                .filter(key -> key.startsWith(keyPrefix))
                .map(sseEmitterMap::get)
                .collect(Collectors.toList());
    }

    public List<String> getKeyListByKeyPrefix(String keyPrefix){
        return sseEmitterMap.keySet().stream()
                .filter(key -> key.startsWith(keyPrefix))
                .collect(Collectors.toList());
    }

    public void remove(String key) {
        sseEmitterMap.remove(key);
    }
}
