package com.victoria.customer.util;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.victoria.customer.R;
import com.victoria.customer.ui.interfaces.CallbackMenu;

/**
 * Created on 6/6/18.
 */
public class PopupViews {
    public static PopupMenu popup;

    public PopupViews() {
    }

    public static void openPupupMenu(Context context, View view, int menu, final CallbackMenu callbackSuccess) {

        popup = new PopupMenu(context, view, Gravity.CENTER | Gravity.END, 0, R.style.popup_menu_style);

        popup.getMenuInflater().inflate(menu, popup.getMenu());
        // popup.inflate(menu);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.itemCash:
                        callbackSuccess.onCallBack(R.id.itemCash);
                        popup.dismiss();
                        return true;
                    /*case R.id.itemCredit:
                        callbackSuccess.onCallBack(R.id.itemCredit);
                        popup.dismiss();
                        return true;*/
                    case R.id.itemMe:
                        callbackSuccess.onCallBack(R.id.itemMe);
                        popup.dismiss();
                        return true;
                    case R.id.itemCard:
                        callbackSuccess.onCallBack(R.id.itemCard);
                        popup.dismiss();
                        return true;
                    case R.id.itemOther:
                        callbackSuccess.onCallBack(R.id.itemOther);
                        popup.dismiss();
                        return true;
                    case R.id.itemWallet:
                        callbackSuccess.onCallBack(R.id.itemWallet);
                        popup.dismiss();
                        return true;


                }
                return false;
            }
        });

        popup.show();
    }

    public static PopupMenu getPopupMenuInstance() {
        if (popup != null) {
            return popup;
        }
        return null;
    }
}
