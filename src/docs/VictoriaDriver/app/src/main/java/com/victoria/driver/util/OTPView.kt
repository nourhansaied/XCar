package com.victoria.driver.util


import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.victoria.driver.ui.interfaces.CallbackSuccess


class OTPView(private var activity: FragmentActivity, internal var callbackSuccess: CallbackSuccess, vararg editTexts: EditText) {
    private var length: Int=0
    private val editTexts: Array<EditText?> = arrayOfNulls(editTexts.size)
    private var  mCurrentlyFocusedEditText: EditText?=null

    internal var onFocusChangeListener: View.OnFocusChangeListener = View.OnFocusChangeListener { view, b ->
        if (b) {
            val m = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
            (view as AppCompatEditText).setSelection(view.text!!.length)
            mCurrentlyFocusedEditText = view
        } /*else {
                InputMethodManager imm =
                        (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }*/
    }

    internal var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if(mCurrentlyFocusedEditText!=null) {
                if (mCurrentlyFocusedEditText!!.text.isNotEmpty() && mCurrentlyFocusedEditText !== editTexts[length - 1]) {
                    mCurrentlyFocusedEditText!!.focusSearch(View.FOCUS_RIGHT).requestFocus()
                } else if (mCurrentlyFocusedEditText!!.text.isNotEmpty() && mCurrentlyFocusedEditText === editTexts[length - 1]) {
                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm?.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)

                    callbackSuccess.onSuccess()

                } else if (mCurrentlyFocusedEditText !== editTexts[0]) {
                    val currentValue = mCurrentlyFocusedEditText!!.text.toString()
                    if (currentValue.isEmpty() && mCurrentlyFocusedEditText!!.selectionStart <= 0) {
                        mCurrentlyFocusedEditText!!.focusSearch(View.FOCUS_LEFT).requestFocus()
                    }
                }
            }
        }
    }

    /**
     * Returns the present otp entered by the user
     *
     * @return OTP
     */
    /**
     * Used to set the OTP. More of cosmetic value than functional value
     *
     * @param otp Send the four digit otp
     */
    var otp: String
        get() = makeOTP()
        set(otp) {
            if (otp.length != length) {
                Log.e("OTPView", "Invalid otp param")
                return
            }
            if (editTexts[0]?.inputType == InputType.TYPE_CLASS_NUMBER && !otp.matches("[0-9]+".toRegex())) {
                Log.e("OTPView", "OTP doesn't match INPUT TYPE")
                return
            }

            for (i in 0 until length) {
                editTexts[i]?.setText(otp[i].toInt())
            }
        }

    init {
        length = editTexts.size
        for (i in editTexts.indices) {
            this.editTexts[i] = editTexts[i]
            this.editTexts[i]?.onFocusChangeListener = onFocusChangeListener
            this.editTexts[i]?.addTextChangedListener(textWatcher)
        }
    }

    /**
     * Get an instance of the present otp
     */
    private fun makeOTP(): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            stringBuilder.append(editTexts[i]?.text.toString())
        }
        return stringBuilder.toString()
    }

    /**
     * Checks if all four fields have been filled
     *
     * @return length of OTP
     */
    fun hasValidOTP(): Boolean {
        return makeOTP().length == length
    }
/*
    private inner class PasswordTransformationMethod(passwordChar: String) : TransformationMethod {

        private val BULLET = '\u2022'
        private var passwordChar = "*"

        init {
            this.passwordChar = passwordChar
        }

        override fun getTransformation(source: CharSequence, view: View): CharSequence {
            return PasswordCharSequence(source)
        }

        override fun onFocusChanged(view: View, sourceText: CharSequence, focused: Boolean, direction: Int, previouslyFocusedRect: Rect) {

        }

        private inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {

            override val length: Int
                return source.length


            override fun charAt(index: Int): Char {
                return passwordChar[0]
            }

            override fun subSequence(start: Int, end: Int): CharSequence {
                return PasswordCharSequence(this.source.subSequence(start, end))
            }
        }
    }*/
}