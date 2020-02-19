package com.victoria.driver.util;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by khushbu on 12/5/17.
 */

public class CalendarUtils {


    public static final String PERSIAN_LANGUAGE = "fa";
    Calendar calendar;
    private final Locale locale;

    public CalendarUtils(Locale locale) {
        this.locale = locale;
        this.calendar = Calendar.getInstance(locale);
    }



    public String getMonthName(int month) {

        this.calendar.set(2, month);
        return this.calendar.getDisplayName(2, 1, this.locale);
    }

    public String[] getMonthList() {

        List<String> strings = new ArrayList();
        for (int v = 0; v <= 11; v++) {
            this.calendar.set(2, v);
            strings.add(this.calendar.getDisplayName(2, 1, this.locale));
        }
        return (String[]) strings.toArray(new String[strings.size()]);
    }

    public List<String> getMonthListing(){

        List<String> strings = new ArrayList();
        for (int v = 0; v <= 11; v++) {
            this.calendar.set(2, v);
            strings.add(this.calendar.getDisplayName(2, 1, this.locale));
        }
        return strings;
    }

    public String[] getWeekList() {

        this.calendar.clear();
        this.calendar.set(1, 2017);
        List<String> strings = new ArrayList();
        for (int v = 1; v <= 7; v++) {
            this.calendar.set(5, v);
            strings.add(this.calendar.getDisplayName(7, 1, this.locale));
        }
        return (String[]) strings.toArray(new String[strings.size()]);
    }



}