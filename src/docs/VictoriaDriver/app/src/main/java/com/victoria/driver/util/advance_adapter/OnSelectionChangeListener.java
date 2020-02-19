package com.victoria.driver.util.advance_adapter;

import android.support.annotation.UiThread;

/**
 * Created by hlink21 on 10/6/16.
 */
public interface OnSelectionChangeListener<T> {

    @UiThread
    void onSelectionChange(T t, boolean isSelected);
}
