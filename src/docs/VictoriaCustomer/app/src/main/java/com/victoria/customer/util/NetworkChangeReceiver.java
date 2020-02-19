package com.victoria.customer.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.victoria.customer.R;


/**
 * Created by darshan on 14/5/15.
 */

public class NetworkChangeReceiver extends android.content.BroadcastReceiver {

    public static Dialog noInternetDialog;
    String status;
    private long mLastClickTime;
    Activity activity;

    @Override
    public void onReceive(Context context, Intent intent) {
        status = NetworkUtil.getConnectivityStatusString(context);
        if (status.equalsIgnoreCase("Not connected to Internet")) {
            if (null == noInternetDialog) {
                createDialog(context);
            } else {
                //createDialog(context);
                noInternetDialog.dismiss();
                createDialog(context);
            }
        } else {
            if (null == noInternetDialog) {
                dissmisDialog();
            } else {
                dissmisDialog();
            }
        }
    }

    public static void createDialog(Context context) {
        noInternetDialog = new Dialog(context);
        noInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noInternetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        noInternetDialog.setContentView(R.layout.dialog_no_internet_layout);
        final ImageView imageView =  noInternetDialog.findViewById(R.id.imageViewRefresh);
        final ImageView imageViewInternet =  noInternetDialog.findViewById(R.id.imageViewInternet);
        TextView textViewRefresh =  noInternetDialog.findViewById(R.id.textViewRefresh);

        noInternetDialog.setCancelable(false);
        noInternetDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        noInternetDialog.show();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation anim = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
                anim.setDuration(700); //Put desired duration per top cycle here, in milliseconds
                imageView.startAnimation(anim);
            }
        };
        imageView.setOnClickListener(onClickListener);
        textViewRefresh.setOnClickListener(onClickListener);
    }

    public static void dissmisDialog() {
        try {
            if (noInternetDialog != null && noInternetDialog.isShowing()) {
                noInternetDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
