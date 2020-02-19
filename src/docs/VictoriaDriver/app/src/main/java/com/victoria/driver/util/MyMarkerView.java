package com.victoria.driver.util;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.victoria.driver.R;


/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private AppCompatTextView tvContent;
    private AppCompatTextView textViewAmount;
    private LinearLayout linearLayoutInfoWindow;


    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.txtInfoWindow);
        textViewAmount = findViewById(R.id.textViewAmount);
        linearLayoutInfoWindow = findViewById(R.id.linearLayoutInfoWindow);

    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    public void setValue(float x) {
        if (x != 0) {
            linearLayoutInfoWindow.setVisibility(VISIBLE);
            textViewAmount.setText(String.valueOf(x));
        } else {
            linearLayoutInfoWindow.setVisibility(GONE);
        }
    }
}
