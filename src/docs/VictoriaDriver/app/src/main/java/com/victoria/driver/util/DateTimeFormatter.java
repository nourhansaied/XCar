package com.victoria.driver.util;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Vishwa on 30/11/17.
 */

public class DateTimeFormatter {

    private static Date date;
    private TimeZone outTimeZone = TimeZone.getDefault();

    private DateTimeFormatter() {

    }

    /**
     * @param date :- Date as String object , Please also provide inFormat if not in format of "yyyy-MM-dd hh:mm:ss"
     * @return class Object
     */
    public static DateTimeFormatter date(String date, String pattern) {
        DateFormat timeFormater = new SimpleDateFormat(pattern);
        timeFormater.setTimeZone(TimeZone.getDefault());
        try {
            DateTimeFormatter.date = timeFormater.parse(date);
        } catch (ParseException e) {
            DateTimeFormatter.date = null;
        }
        return new DateTimeFormatter();
    }

    /**
     * @param date :- Date as Date object
     * @return class Object
     */
    public static DateTimeFormatter date(Date date) {
        DateTimeFormatter.date = date;
        return new DateTimeFormatter();
    }

    public DateTimeFormatter timeZoneToConvert(String timezone) {
        outTimeZone = TimeZone.getTimeZone(timezone);
        return this;
    }


    public static String dateAndTimeGet(String convertDateTime, String convertFormate) {
        String time = "";
        try {
            long now = System.currentTimeMillis();
            String datetime1 = convertDateTime;
            SimpleDateFormat dateFormat = new SimpleDateFormat(convertFormate);
            Date convertedDate = dateFormat.parse(datetime1);
            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(convertedDate.getTime(), now, DateUtils.SECOND_IN_MILLIS);
            time = "" + relavetime1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String dateTimeConvertLocalToLocal(String timeConvert, String input, String output) {
        DateFormat timeFormater = new SimpleDateFormat(input);
        try {
            Date time;
            timeFormater.setTimeZone(TimeZone.getDefault());
            time = timeFormater.parse(timeConvert);
            DateFormat timeFormaterSecond = new SimpleDateFormat(output);
            return timeFormaterSecond.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public DateTimeFormatter timeZoneToConvert(TimeZone timezone) {
        outTimeZone = timezone;
        return this;
    }

    public String formatDateToLocalTimeZone(String format) {
        return new SimpleDateFormat(format, Locale.US).format(date);
    }

    public String formatDateToTimeZone(String format) {
        TimeZone utc = outTimeZone;
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        formatter.setTimeZone(utc);
        return formatter.format(date);
    }

    public String timeIn12Hoursformat() {
        TimeZone utc = outTimeZone;
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.US);
        formatter.setTimeZone(utc);
        return formatter.format(date);
    }

    public String timeIn24HoursformatWithoutAmPm() {
        TimeZone utc = outTimeZone;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
        formatter.setTimeZone(utc);
        return formatter.format(date);
    }

    public String timeIn24Hoursformat() {
        TimeZone utc = outTimeZone;
        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm", Locale.US);
        formatter.setTimeZone(utc);
        return formatter.format(date);
    }


    public static String utcToLocal(String dateString, String inputDateFormat, String outputDateFormat) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inputDateFormat);//"yyyy-MM-dd HH:mm:ss"
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // TimeZone.getDefault()
        Date date = null;
        String dateAsString = "";
        String dateS = null;
        try {
            date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simple = new SimpleDateFormat(outputDateFormat, Locale.ENGLISH);//"yyyy-MM-dd HH:mm:ss"
            TimeZone utcZone = TimeZone.getDefault(); // TimeZone.getTimeZone("UTC");
            simple.setTimeZone(utcZone);
            dateS = simple.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateS;
    }
}