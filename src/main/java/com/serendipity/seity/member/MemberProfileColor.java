package com.serendipity.seity.member;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

/**
 * 사용자의 프로필 사진 색상을 나타내는 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
public enum MemberProfileColor {

    LUSTY_GALLANT("#FD7F4F", "#FFFFFF"),
    LABRADOR("#809EAD", "#FFFFFF"),
    TWILIGHT_PURPLE("#66648B", "#FFFFFF"),
    ANTIQUE_WHITE("#FAEBD7", "#000000"),
    KHAKI("#C3B091", "#000000"),
    POWDER_BLUE("#B4D8E7", "#000000"),
    MAUVE("#915F6D", "#FFFFFF"),
    WHEAT("#E3C7A6", "#000000"),
    PEACH_ECHO("#F7786B", "#FFFFFF"),
    APRICOT("#FF9945", "#FFFFFF");

    private static final Random random = new Random();
    private final String backgroundHex;
    private final String textHex;

    /**
     * 랜덤한 1개의 색상을 추출하는 메서드입니다.
     * @return 랜덤 색상
     */
    public static MemberProfileColor getRandomColor() {

        return values()[random.nextInt(values().length)];
    }
}
