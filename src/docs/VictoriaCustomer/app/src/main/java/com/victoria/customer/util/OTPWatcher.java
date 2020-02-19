package com.victoria.customer.util;

import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.victoria.customer.R;

import static android.content.ContentValues.TAG;

/**
 * Created by sanket on 25/11/16.
 */

public class OTPWatcher implements View.OnKeyListener {


    private final AppCompatEditText editTextOTP1;
    private final AppCompatEditText editTextOTP2;
    private final AppCompatEditText editTextOTP3;
    private final AppCompatEditText editTextOTP4;
    private OTPListner otpListner;

    public OTPWatcher(AppCompatEditText editTextOTP1, AppCompatEditText editTextOTP2,
                      AppCompatEditText editTextOTP3, AppCompatEditText editTextOTP4, OTPListner otpListner) {
        this.editTextOTP1 = editTextOTP1;
        this.editTextOTP2 = editTextOTP2;
        this.editTextOTP3 = editTextOTP3;
        this.editTextOTP4 = editTextOTP4;
        this.otpListner = otpListner;
        editTextOTP1.addTextChangedListener(new OTPTextWatcher(editTextOTP1, editTextOTP2));
        editTextOTP2.addTextChangedListener(new OTPTextWatcher(editTextOTP2, editTextOTP3));
        editTextOTP3.addTextChangedListener(new OTPTextWatcher(editTextOTP3, editTextOTP4));
        editTextOTP1.setOnKeyListener(this);
        editTextOTP2.setOnKeyListener(this);
        editTextOTP3.setOnKeyListener(this);
        editTextOTP4.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
            Log.d(TAG, "onKey() called with: view = [" + view + "], keyCode = [" + keyCode + "], keyEvent = [" + keyEvent + "]");
            checkOTP();
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                switch (view.getId()) {
                    case R.id.editTextOtp2:
                        editTextOTP1.requestFocus();
                        editTextOTP1.setSelection(editTextOTP1.getText().length());
                        break;
                    case R.id.editTextOtp3:
                        editTextOTP2.requestFocus();
                        editTextOTP2.setSelection(editTextOTP2.getText().length());
                        break;
                    case R.id.editTextOtp4:
                        editTextOTP3.requestFocus();
                        editTextOTP3.setSelection(editTextOTP3.getText().length());
                        break;
                }
            }
        }
        return false;
    }

    private void checkOTP() {
        String s = editTextOTP1.getText().toString().trim() + editTextOTP2.getText().toString().trim() + editTextOTP3.getText().toString().trim() + editTextOTP4.getText().toString().trim();
        if (s.trim().length() == 4) {
            otpListner.onvalidOTP(s);
        } else {
            otpListner.onInavlidOTP(s);
        }
    }

    public interface OTPListner {
        void onvalidOTP(String s);

        void onInavlidOTP(String s);
    }

    private class OTPTextWatcher implements TextWatcher {
        private final EditText currentEditText;
        private final EditText nextEditText;

        private OTPTextWatcher(EditText currentEditText, EditText nextEditText) {
            this.currentEditText = currentEditText;
            this.nextEditText = nextEditText;
            currentEditText.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 1) {
                nextEditText.setText(String.valueOf(editable.toString().charAt(editable.toString().length() - 1)));

                currentEditText.setText(editable.toString().substring(0, editable.toString().length() - 1));
                nextEditText.requestFocus();
                nextEditText.setSelection(nextEditText.getText().length());
            }
        }
    }
}