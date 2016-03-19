package com.firstlinesoftware.delivery.util;

import org.apache.commons.lang3.StringUtils;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public class CityCodeUtil {
    public static final int keyCityCodeLength = 5;
    public static final char keyCityCodePadChar = '0';

    public static String padCityCode(int cityCode) {
        return StringUtils.leftPad(String.valueOf(cityCode), keyCityCodeLength, keyCityCodePadChar);
    }
}
