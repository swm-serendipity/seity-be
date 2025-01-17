package com.serendipity.seity.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptConfig {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private static final String[] CHAT_MODEL = { "gpt-3.5-turbo", "gpt-4" };
    public static final Integer MAX_TOKEN = 1000;
    public static final Boolean STREAM = true;
    public static final String USER_ROLE = "user";
    public static final String ASSISTANT_ROLE = "assistant";
    public static final Double TEMPERATURE = 0.6; // 최대 1.0
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String NGINX_NO_BUFFERING_HEADER = "X-Accel-Buffering";
    public static final String NGINX_NO_BUFFERING_HEADER_VALUE = "no";
    public static final String DONE_MESSAGE = "[DONE]";
    public static final String PROCEEDING_RESPONSE = "proceeding";
    public static final int MAX_TOKEN_SIZE = 4000;

    public static String getChatModel(String chatModel) {

        for (String model : CHAT_MODEL) {

            if (chatModel.equals(model)) {
                return model;
            }
        }

        // invalid한 값을 입력했을 경우 default로 gpt 3.5 turbo로 동작
        return "gpt-3.5-turbo";
    }
}
