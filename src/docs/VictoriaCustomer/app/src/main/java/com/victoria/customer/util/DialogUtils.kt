package com.victoria.customer.util


import android.app.Activity
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.ui.interfaces.CallbackLocationSelection
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*


/**
 * Created on 7/6/18.
 */
object DialogUtils {

    /**
     * -----------------------This method used to open ImagePicker1 ------------------------------------
     */
    fun openImagePicker(context: Fragment, imagePickerResult: ImagePicker1.ImagePickerResult): ImagePicker1 {

        val imagePicker1 = ImagePicker1()
        imagePicker1.setImagePickerResult(imagePickerResult)
        imagePicker1.show(context.activity!!.supportFragmentManager, ImagePicker1::class.java.simpleName)
        return imagePicker1
    }


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

  /*  fun openTimePicker(activity: Activity, onTimeSetListener: TimePickerDialog.OnTimeSetListener, calendar: Calendar, needMinimum: Boolean) {

        val now = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog.newInstance(onTimeSetListener, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
                true)
        timePickerDialog.show(activity.fragmentManager, "")

        if (needMinimum) {

            timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        }
        // timePickerDialog.setThemeDark(true);

    }*/


    fun getScreenWidthHeight(activity: Activity) : Int{
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return height
    }


    /**------------------------------This is used for display subCategory of sounds selection------------------------------------------ */
    fun  showBottomSheetDialog(activity: Activity, itemEventListener: CallbackLocationSelection?) {

        val view = activity.layoutInflater.inflate(R.layout.bottom_sheet_select_location, null)

        val mBottomSheetDialog = BottomSheetDialog(activity)

        mBottomSheetDialog.setContentView(view)
        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        //Set callback
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            val displaymetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
            val screenHeight = displaymetrics.heightPixels
            behavior.peekHeight = 600

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            //behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }


        mBottomSheetDialog.show()
    }


}
