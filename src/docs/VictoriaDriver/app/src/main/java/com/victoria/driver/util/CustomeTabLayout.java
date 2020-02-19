package com.victoria.driver.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.victoria.driver.R;


/**
 * Created by khushbu on 3/5/17.
 */

public class CustomeTabLayout extends TabLayout {
    private Typeface mTypeface;

    public CustomeTabLayout(Context context) {
        super(context);
    }

    public CustomeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {


        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontStyle);
            try {
                String fontName = a.getString(R.styleable.FontStyle_fontName);
                if (fontName != null) {
                    mTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                }

            } finally {
                a.recycle();
            }
        }


        // here you will provide fully qualified path for fonts
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
        super.setupWithViewPager(viewPager);

        if (mTypeface != null) {
            this.removeAllTabs();
            ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

            PagerAdapter adapter = viewPager.getAdapter();

            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                Tab tab = this.newTab();
                this.addTab(tab.setText(adapter.getPageTitle(i)));
                AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                view.setTypeface(mTypeface, Typeface.NORMAL);

            }
        }
    }

}