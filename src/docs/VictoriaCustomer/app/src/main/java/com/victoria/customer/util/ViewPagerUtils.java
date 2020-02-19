package com.victoria.customer.util;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created on 30/11/18.
 */
public class ViewPagerUtils {
    public static View getCurrentView(ViewPager viewPager) {
        final int currentItem = viewPager.getCurrentItem();
        for (int i = 0; i < viewPager.getChildCount(); i++) {
            final View child = viewPager.getChildAt(i);
            final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();
            if (!layoutParams.isDecor /*&& currentItem == layoutParams.position*/) {
                return child;
            }
        }
        return null;
    }
}