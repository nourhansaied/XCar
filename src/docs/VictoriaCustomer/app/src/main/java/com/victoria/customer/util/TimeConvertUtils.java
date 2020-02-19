package com.victoria.customer.util;

import android.text.format.DateUtils;
import android.util.Log;

import com.victoria.customer.core.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by  on 2/7/16.
 */
public class TimeConvertUtils {
    /**
     * @param timeConvert: Time to be Converted
     * @param input:       Input format
     * @param output:      Output format
     * @return formated date time
     */
    public static String dateTimeConvertServertToLocal(String timeConvert, String input, String output) {

        //  DebugLog.e("Convert Time" + timeConvert);

        DateFormat timeFormater = new SimpleDateFormat(input);

        try {

            Date time;
            timeFormater.setTimeZone(TimeZone.getDefault());

           /* if (URLFactory.isLocal)
            timeFormater.setTimeZone(TimeZone.getTimeZone(Common.SERVER_TIMEZONE));*/


            time = timeFormater.parse(timeConvert);

            DateFormat timeFormaterSecond = new SimpleDateFormat(output); //HH for hour of the day (0 - 23)

            timeFormaterSecond.setTimeZone(TimeZone.getDefault());


            return timeFormaterSecond.format(time);


        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        return "";
    }

    /**
     * @param timeConvert: Time to be Converted
     * @param input:       Input format
     * @param output:      Output format
     * @return formated date time
     */
    public static String dateTimeConvertLocalToServer(String timeConvert, String input, String output) {


        DateFormat timeFormater = new SimpleDateFormat(input);

        try {

            Date time;

            timeFormater.setTimeZone(TimeZone.getDefault());

            time = timeFormater.parse(timeConvert);

            DateFormat timeFormaterSecond = new SimpleDateFormat(output);

            timeFormaterSecond.setTimeZone(TimeZone.getTimeZone(Common.SERVER_TIMEZONE));

            Log.e("", timeFormaterSecond.format(time));

            return timeFormaterSecond.format(time);


        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        return "";
    }

    /**
     * @param timeConvert: Time to be Converted
     * @param input:       Input format
     * @param output:      Output format
     * @return formated date time
     */
    public static String dateTimeConvertLocalToLocal(String timeConvert, String input, String output) {


        DateFormat timeFormater = new SimpleDateFormat(input);

        try {

            Date time;

            timeFormater.setTimeZone(TimeZone.getDefault());

            time = timeFormater.parse(timeConvert);

            DateFormat timeFormaterSecond = new SimpleDateFormat(output);


            Log.e("++++++++", timeFormaterSecond.format(time));

            return timeFormaterSecond.format(time);


        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        return "";
    }

    /**
     * @param convertDateTime: Time to be Converted
     * @param convertFormate:  Input format
     * @return formated date time
     */
    public static String dateAndTimeGet(String convertDateTime, String convertFormate) {
        String time = "";
        try {
            long now = System.currentTimeMillis();
            String datetime1 = convertDateTime;
            SimpleDateFormat dateFormat = new SimpleDateFormat(convertFormate);
            Date convertedDate = dateFormat.parse(datetime1);

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            time = "" + relavetime1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

}