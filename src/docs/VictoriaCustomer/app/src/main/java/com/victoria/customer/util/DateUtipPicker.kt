package com.victoria.customer.util

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.victoria.customer.BuildConfig
import com.victoria.customer.R
import com.victoria.customer.core.Common
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateUtipPicker {
    private val edit_position: Int = 0
    private val p = Paint()

    /**
     * This method is used for to open spinner
     */
    /* public void openSpinnerpopup( String header, final List<String> dataList, final ISpinnerItemClick callback) {

        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, dataList);

        new AlertDialog.Builder(context)
                .setTitle(header)
                .setAdapter(spinner_countries, new DialogInterface.OnAppointmentClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        callback.onItemClick(dataList.get(which));
                        dialog.dismiss();
                    }
                }).create().show();
    }*/
    fun getDateWithSuffix(day: Int): String {

        if (day >= 11 && day <= 13) {
            return "th"
        }
        when (day % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th"
        }
    }


    /**
     * it return encoaded Uri
     *
     * @param thisUrl
     * @return
     */
    fun getEncoadedURI(thisUrl: String): Uri {
        var url: URL? = null
        try {
            url = URL(thisUrl)
        } catch (e: MalformedURLException) {

            e.printStackTrace()
        }

        var builder: Uri.Builder? = null
        if (url != null) {
            builder = Uri.Builder()
                    .scheme(url.protocol)
                    .authority(url.authority)
                    .appendPath(url.path)
        }
        return builder!!.build()
    }


    fun utcToLocal(dateString: String): String? {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        var date: Date? = null
        val dateAsString = ""
        var dateS: String? = null
        // Timber.e("Date is ::::::::::::::: $dateString")
        try {
            date = simpleDateFormat.parse(dateString)
            val simple = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
            val utcZone = TimeZone.getDefault()
            simple.timeZone = utcZone
            dateS = simple.format(date)
            // Timber.e("Date after format is :::::::::::::::::: " + dateS!!)
            //  TimberLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            // TimberLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateS
    }

    fun utcToLocalTime(dateString: String): String? {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        var date: Date? = null
        val dateAsString = ""
        var dateS: String? = null
        //Timber.e("Time is :::::::::::::::::::: $dateString")
        try {
            date = simpleDateFormat.parse(dateString)
            val simple = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val utcZone = TimeZone.getDefault()
            simple.timeZone = utcZone
            dateS = simple.format(date)
            //Timber.e("Time format is ::::::::::::: " + dateS!!)
            //  TimberLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            // TimberLog.e("new " + dateS + "" + " " + simple.getTimeZone());
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateS
    }

    fun getDifferentBetweenTwoDate(current: Date, nextDate: Date): Long {
        val diff = nextDate.time - current.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        // Timber.e("diff ::$diff")
        //Timber.e("minutes ::$minutes")
        return minutes
    }

    fun localToUTC24(dateString: String): String? {
        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val utcZone = TimeZone.getDefault()
        simpleDateFormat.timeZone = utcZone

        var date: Date? = null
        val dateAsString = ""
        var dateS: String? = null
        try {
            date = simpleDateFormat.parse(dateString)
            val simple = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            simple.timeZone = TimeZone.getTimeZone("GMT")
            dateS = simple.format(date)
            //            TimberLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            //Timber.e("new " + dateS + "" + " " + simple.timeZone)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateS
    }

    fun localToUTCDate(dateString: String): String {
        val simpleDateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
        val utcZone = TimeZone.getDefault()
        simpleDateFormat.timeZone = utcZone
        // Timber.e(" Date is :::::::::::::::: $dateString")
        val date: Date
        var dateS = ""

        try {
            date = simpleDateFormat.parse(dateString)
            // Timber.e(" Date After Parsing is :::::::::::::: $date")
            val simple = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            /* simple.setTimeZone(TimeZone.getTimeZone("GMT"));*/
            // Timber.e(" Formatting date :::::::::::: " + simple.format(date))
            dateS = simple.format(date)

            //            TimberLog.e("time" + String.valueOf(date) + "" + simpleDateFormat.format(date) + " " + utcZone);
            // Timber.e("new " + dateS + "" + " " + simple.timeZone)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateS
    }

    /* public UserWrapper getData() {
        try {
            return new Gson().fromJson(getSharedPref(Constants.USER), UserWrapper.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String userSessionId() {
        if (getData() != null) {
            return getData().getData().getUserSessionId();
        } else
            return "";
    }

    public String userId() {
        if (getData() != null)
            return getData().getData().getId();
        else
            return "";
    }
*/
    fun parseDate(date: Date): String {
        try {//dd MMM yyyy
            return SimpleDateFormat("dd MMM, yyyy").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun parseDateForDisplay(date: String): String {
        val form = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date1 = form.parse(date)
            return parseDate(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    fun parseTime(date: Date): String {
        try {//dd MMM yyyy
            return SimpleDateFormat("hh:mm a").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun parseTimeToDisplay(date: String): String {
        val form = SimpleDateFormat("HH:mm")
        try {
            val date1 = form.parse(date)
            return parseTime(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    fun parseDateString(date: Date): String {
        try {//dd MMM yyyy
            return SimpleDateFormat("yyyy-MM-dd").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun parseDateForDisplayString(date: String): String {
        val form = SimpleDateFormat("dd MMMM,   yyyy")
        try {
            val date1 = form.parse(date)
            return parseDateString(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    companion object {

        val instance = DateUtipPicker()
    }


}