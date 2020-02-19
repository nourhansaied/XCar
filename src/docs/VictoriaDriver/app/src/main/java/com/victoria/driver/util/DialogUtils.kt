package com.victoria.driver.util

import android.app.Activity

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*


/**
 * Created on 7/6/18.
 */
object DialogUtils {

    /**
     * -----------------------This method used to open ImagePicker1 ------------------------------------
     */

    fun openDatePicker(activity: Activity, onDateSetListener: DatePickerDialog.OnDateSetListener, minDate: Calendar?) {

        //Calendar now = Calendar.getInstance();

        if (minDate != null) {
            val dpd = DatePickerDialog.newInstance(onDateSetListener,
                    minDate.get(Calendar.YEAR),
                    minDate.get(Calendar.MONTH),
                    minDate.get(Calendar.DAY_OF_MONTH)

                    // dpd.set
            )
            dpd.show(activity.fragmentManager, "Datepickerdialog")
            if (minDate != null)
                dpd.minDate = minDate
        } else {

            val now = Calendar.getInstance()

            val dpd = DatePickerDialog.newInstance(onDateSetListener,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show(activity.fragmentManager, "Datepickerdialog")


        }

        //dpd.setThemeDark(true);
    }

    fun openTimePicker(activity: Activity, onTimeSetListener: TimePickerDialog.OnTimeSetListener, calendar: Calendar, needMinimum: Boolean) {

        val now = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
                true)
        timePickerDialog.show(activity.fragmentManager, "")

        if (needMinimum) {

            timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        }
        // timePickerDialog.setThemeDark(true);

    }


}
