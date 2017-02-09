package com.ider.cloudreader.common;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ider-eric on 2017/2/9.
 */

public class DateFormatter {

    private static final String TAG = "DateFormatter";

    private static HashMap<String, String> monthMap;

    static {
        monthMap = new HashMap<>();
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");

    }

    /* 将Thu Feb 09 14:02:46 +0800 2017类型字符串转换为Date类型 */
    private static Date string2Date(String string) {
        String[] split = string.split(" ");
        String year = split[5];
        String month = monthMap.get(split[1]);
        String day = split[2];
        String time = split[3];
        String strDate = String.format("%s|%s|%s|%s", year, month, day, time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy|MM|dd|HH:mm:ss");
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getInterval(String createAt) {
        Date createDate = string2Date(createAt);
        if(createDate != null) {
            long createMills = createDate.getTime();
            long currentMills = System.currentTimeMillis();
            long intervalSecond = (currentMills - createMills) / 1000;
            Log.i(TAG, "getInterval: " + intervalSecond);
            int day = (int) (intervalSecond / 60 / 60 / 24);
            int hour = (int) (intervalSecond / 60 / 24);
            int minute = (int) (intervalSecond / 60);

            if(day != 0) {
                return String.format("%d天前", day);
            } else if (hour != 0) {
                return String.format("%d小时前", hour);
            } else {
                minute = minute == 0 ? 1 : minute;
                return String.format("%d分钟前", minute);
            }

        }

        return createAt;

    }

}
