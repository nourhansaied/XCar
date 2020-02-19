package com.victoria.customer.ui.home.dialog

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.core.Session
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.ui.manager.Navigator
import kotlinx.android.synthetic.main.dialog_fragment_select_payment_mode.*
import kotlinx.android.synthetic.main.toolbar_with_back.*


@SuppressLint("ValidFragment")
/**
 * Created by hlink53 on 19/5/16.
 */
class ChangePaymentMethodPopup @SuppressLint("ValidFragment") constructor
(var callBackForLanguageSelect: CallBackForLanguageSelect,
 var appPreferences: AppPreferences, var session: Session,
 var navigator: Navigator,
 var paymentMethod: String) : DialogFragment() {
    internal lateinit var sharedPreferences: SharedPreferences
    var paymentMode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_select_payment_mode, container, false)

        val wmlp = dialog.window!!.attributes
        dialog.window!!.requestFeature(STYLE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onStart() {
        super.onStart()
        val params = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.paymentMode = paymentMethod
        checkBoxCash.typeface = context?.let { ResourcesCompat.getFont(it, R.font.montserrat_regular) }
        checkBoxCredit.typeface = context?.let { ResourcesCompat.getFont(it, R.font.montserrat_regular) }
        checkBoxWallet.typeface = context?.let { ResourcesCompat.getFont(it, R.font.montserrat_regular) }
        checkBoxWallet.setOnClickListener {
            paymentMode = "wallet"
        }
        checkBoxCredit.setOnClickListener {
            paymentMode = "card"
        }
        checkBoxCash.setOnClickListener {
            paymentMode = "cash"
        }
        when {
            VictoriaCustomer.getTripData().paymentMode == "card" -> {
                paymentMode = "card"
                checkBoxCredit.isChecked = true
            }
            VictoriaCustomer.getTripData().paymentMode == "cash" -> {
                paymentMode = "cash"
                checkBoxCash.isChecked = true
            }
            else -> {
                paymentMode = "wallet"
                checkBoxWallet.isChecked = true
            }
        }
        imageViewMenu.setOnClickListener {
            dismissAllowingStateLoss()
        }

        buttonAddCard.setOnClickListener {
            dismissAllowingStateLoss()
            callBackForLanguageSelect.dissmissDialog(paymentMode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    interface CallBackForLanguageSelect {
        fun dissmissDialog(paymentMode: String)
    }
}
