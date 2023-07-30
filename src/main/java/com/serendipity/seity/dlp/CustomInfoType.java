package com.serendipity.seity.dlp;

import com.serendipity.seity.dlp.dto.api.request.InfoType;
import com.serendipity.seity.dlp.dto.api.request.Regex;
import com.serendipity.seity.dlp.dto.api.request.SingleCustomInfoType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Google DLP에 추가로 정의할 Info type 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
public enum CustomInfoType {

    REGISTRATION_NUMBER("REGISTRATION_NUMBER", "(\\d{6}[ ,-]-?[1-4]\\d{6})|(\\d{6}[ ,-]?[1-4])"),
    DRIVER_LICENSE_NUMBER("DRIVER_LICENSE_NUMBER", "(\\d{2}-\\d{2}-\\d{6}-\\d{2})"),
    STREET_NAME_ADDRESS("STREET_NAME_ADDRESS",
            "(([가-힣A-Za-z·\\d~\\-\\.]{2,}(로|길).[\\d]+)|([가-힣A-Za-z·\\d~\\-\\.]+(읍|동)\\s)[\\d]+)"),
    LOT_NUMBER_ADDRESS("LOT_NUMBER_ADDRESS",
            "(([가-힣A-Za-z·\\d~\\-\\.]+(읍|동)\\s)[\\d-]+)|(([가-힣A-Za-z·\\d~\\-\\.]+(읍|동)\\s)[\\d][^시]+)"),
    KOREA_PHONE_NUMBER("KOREA_PHONE_NUMBER", "(\\d{2,3}[ ,-]-?\\d{2,4}[ ,-]-?\\d{4})"),
    ACCOUNT_NUMBER("ACCOUNT_NUMBER", "([0-9,\\-]{3,6}\\-[0-9,\\-]{2,6}\\-[0-9,\\-])"),
    INSURANCE_NUMBER("INSURANCE_NUMBER", "[1257][-~.[:space:]][0-9]{10}"),
    CAR_NUMBER_1("CAR_NUMBER_1", "^[가-힣]{2}\\\\d{2}[가-힣]{1}\\\\d{4}$"),
    CAR_NUMBER_2("CAR_NUMBER_2", "^\\\\d{2}[가-힣]{1}\\\\d{4}$"),
    IPV4_ADDRESS("IPV4_ADDRESS",
            "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}"),
    IPV6_ADDRESS("IPV6_ADDRESS",
            "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:" +
                    "[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}" +
                    "(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:)" +
                    "{1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4})" +
                    "{1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|" +
                    "(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]" +
                    "{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9])" +
                    "{0,1}[0-9]))"),
    MAC_ADDRESS("MAC_ADDRESS", "([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}"),
    LOGIN_ID("LOGIN_ID", "^[A-Za-z]{1}[A-Za-z0-9]{3,19}$"),
    MILITARY_REGISTRATION_NUMBER("MILITARY_REGISTRATION_NUMBER", "^[0-9a-zA-Z]+([_0-9a-zA-Z]+)*$"),
    COMPANY_REGISTRATION_NUMBER("COMPANY_REGISTRATION_NUMBER", "^(\\d{3,3})+[-]+(\\d{2,2})+[-]+(\\d{5,5})");

    private final String value;
    private final String regex;

    public static List<SingleCustomInfoType> getAllValues() {

        List<SingleCustomInfoType> result = new ArrayList<>();
        for (CustomInfoType infoType : values()) {
            result.add(new SingleCustomInfoType(new InfoType(infoType.value), new Regex(infoType.regex)));
        }

        return result;
    }

    public static List<InfoType> getAllValuesToInfoType() {

        List<InfoType> result = new ArrayList<>();

        for (CustomInfoType infoType : values()) {

            result.add(new InfoType(infoType.value));
        }

        return result;
    }
}
