package com.firstlinesoftware.delivery.util;

import org.apache.commons.lang3.StringUtils;

/**
 * User: Legohuman
 * Date: 14/03/16
 */
public class NumberParser {

    public static Double parseDouble(String str) {
        try {
            return new Double(StringUtils.trimToEmpty(str).replaceAll(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long parseLong(String str) {
        try {
            return new Long(StringUtils.trimToEmpty(str));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInt(String str) {
        try {
            return new Integer(StringUtils.trimToEmpty(str));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
