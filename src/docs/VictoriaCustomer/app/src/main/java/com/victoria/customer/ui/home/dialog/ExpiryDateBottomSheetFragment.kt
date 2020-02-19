package com.victoria.customer.ui.home.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import com.victoria.customer.R
import java.util.*


class ExpiryDateBottomSheetFragment : BottomSheetDialogFragment {

    internal lateinit var numberPickerYear: NumberPicker
    internal lateinit var numberPickerMonth: NumberPicker
    internal lateinit var button: Button


    internal lateinit var onSelectedListener: OnSelectedListener

    //Bottom Sheet Callback
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    interface OnSelectedListener {

        fun onSelected(month: Int, year: Int)
    }

    constructor()

    @SuppressLint("ValidFragment")
    constructor(onSelectedListener: OnSelectedListener) {
        this.onSelectedListener = onSelectedListener
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        //super.setupDialog(dialog, style);
        //Get the content View
        val contentView = View.inflate(context, R.layout.bottom_sheet_expiry_date, null)
        numberPickerYear = contentView.findViewById(R.id.numberPickerYear)
        numberPickerMonth = contentView.findViewById(R.id.numberPickerMonth)
        button = contentView.findViewById(R.id.btnSet)
        dialog.setContentView(contentView)

        //Set the coordinator layout behavior
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        //Set callback
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val calendar = Calendar.getInstance()

        numberPickerMonth.minValue = calendar.get(Calendar.MONTH) + 1
        numberPickerMonth.maxValue = calendar.getActualMaximum(Calendar.MONTH) + 1
        numberPickerMonth.wrapSelectorWheel = false
        numberPickerYear.minValue = calendar.get(Calendar.YEAR)
        numberPickerYear.maxValue = calendar.get(Calendar.YEAR) + 50

        numberPickerYear.wrapSelectorWheel = false


        numberPickerYear.setOnValueChangedListener { numberPicker, i, i1 ->
            if (i1 == calendar.get(Calendar.YEAR)) {
                numberPickerMonth.minValue = calendar.get(Calendar.MONTH) + 1
            } else {
                numberPickerMonth.minValue = calendar.getActualMinimum(Calendar.MONTH) + 1
            }
            //numberPickerMonth.setValue(numberPickerMonth.getMinValue());
        }

        button.setOnClickListener { view ->
            onSelectedListener.onSelected(numberPickerMonth.value, numberPickerYear.value)
            dismiss()
        }
    }

}