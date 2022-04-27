package com.hyunshop.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getStringFromDate(Date date, String datePattern) {
        return new SimpleDateFormat(datePattern).format(date);
    }

    public static String getStringFromLongDate(Long longDate, String datePattern) {
        return getStringFromDate(new Date(longDate), datePattern);
    }


}
