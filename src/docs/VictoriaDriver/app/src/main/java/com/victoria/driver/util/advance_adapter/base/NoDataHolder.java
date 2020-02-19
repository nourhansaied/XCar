package com.victoria.driver.util.advance_adapter.base;

import android.view.View;
import android.widget.RelativeLayout;

public class NoDataHolder extends BaseHolder {
    public RelativeLayout errorTextView;

    public NoDataHolder(View itemView) {
        super(itemView);
        errorTextView = (RelativeLayout) itemView;
    }

    public void setErrorText(String errorText) {
        if (errorTextView != null) {
            //errorTextView.setText(errorText);
        }
    }
}