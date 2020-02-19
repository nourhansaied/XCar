package com.victoria.driver.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.victoria.driver.R;


/**
 * Created by Chirag Solanki on 01/07/2015.
 */
public class CustomeTextViewRegular extends android.support.v7.widget.AppCompatTextView {
    public CustomeTextViewRegular(Context context) {
        super(context);
        //   if(isInEditMode())

    }

    public CustomeTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(attrs);
    }

    public CustomeTextViewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //if(isInEditMode())
        setFont(attrs);
    }

    private void setFont(AttributeSet attrs) {


        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontStyle);
            try {
                String fontName = a.getString(R.styleable.FontStyle_fontName);
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                    this.setTypeface(myTypeface);

                }

            } finally {
                a.recycle();
            }

        }

    }
}
