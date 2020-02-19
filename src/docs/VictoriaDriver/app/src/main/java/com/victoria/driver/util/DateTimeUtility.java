package com.victoria.driver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hlink21 ic_on 31/8/17.
 */

public class DateTimeUtility {

    public static String convert24To12(Date time) {
        if (time == null) return null;
        return new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(time);
    }

    public static String convert24To12_UTC(Date time) {
        if (time == null) return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(getTargetTimeZone());
        return simpleDateFormat.format(time);
    }

    public static String formatDate(Date date) {
        if (date == null) return null;
        return new SimpleDateFormat("dd MMM, yyyy ", Locale.getDefault()).format(date);
    }

    public static String format_UTC(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        simpleDateFormat.setTimeZone(getTargetTimeZone());
        return simpleDateFormat.format(date);
    }

    public static String format_UTC(Date date, String pattern, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        simpleDateFormat.setTimeZone(getTargetTimeZone());
        return simpleDateFormat.format(date);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static Date parseTime(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);


            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            simpleDateFormat.setTimeZone(getTargetTimeZone());
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
            simpleDateFormat.setTimeZone(getTargetTimeZone());
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TimeZone getTargetTimeZone() {
        return TimeZone.getTimeZone("GMT+4:00");
    }

    public static String utcToLocalDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
            TimeZone utcZone = TimeZone.getDefault();
            simple.setTimeZone(utcZone);
            dateS = simple.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }

    public static String utcToLocalTime(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            TimeZone utcZone = TimeZone.getDefault();
            simple.setTimeZone(utcZone);
            dateS = simple.format(date);
            //  DebugLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            // DebugLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }


    public static String convertDateFormate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            TimeZone utcZone = TimeZone.getDefault();
            simple.setTimeZone(utcZone);
            dateS = simple.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }


}
