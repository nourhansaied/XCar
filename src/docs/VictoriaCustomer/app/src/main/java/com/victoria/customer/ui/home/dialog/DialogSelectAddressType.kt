package com.victoria.customer.ui.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.TextView
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.ui.base.BaseActivity
import kotlinx.android.synthetic.main.bottom_sheet_select_address_type.*


/**
 * Created  on 19/04/2016.
 */
class DialogSelectAddressType : DialogFragment(),View.OnClickListener {


    lateinit var callBackInterface: CallBackInterface
     var address:String?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_select_address_type, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //getDialog().getWindow().setWindowAnimations(R.style.windowAnimationCardFlip);

        dialog.window!!.setWindowAnimations(
                R.style.dialog_animation_fade)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(view!=null){
            if(address!=null) {
                textViewPlaceAddress.text = address!!
                textViewWork.setOnClickListener(this::onClick)
                textViewHome.setOnClickListener(this::onClick)
                textViewOther.setOnClickListener(this::onClick)
                txtCancel.setOnClickListener(this::onClick)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        window.attributes = lp
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.textViewWork ->{
                callBackInterface.onAddressTypeSelected(Common.AddressType.WORK)
                dismiss()

            }
            R.id.textViewHome ->{
                callBackInterface.onAddressTypeSelected(Common.AddressType.HOME)
                dismiss()
            }

            R.id.textViewOther ->{
                callBackInterface.onAddressTypeSelected(Common.AddressType.OTHER)
                dismiss()
            }
            R.id.txtCancel -> {

                dismiss()
            }
        }
    }




    fun showMsg(msg: String) {
        if (view != null) {
            val snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_LONG)
            val view1 = snackbar.view
            val tv = view1.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
            tv.setTextColor(Color.BLACK)
            view1.setBackgroundColor(Color.WHITE)
            snackbar.show()
        }
    }

    private fun showSnackBar(message: String) {


        /*  if (getView() != null) {

            final Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(getActivity().getResources().getColor(R.color.white));
            snackbar.setAction(getActivity().getResources().getString(R.string.snackbar_action_ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            View snackView = snackbar.getView();
            TextView textView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(4);
            textView.setTextColor(getActivity().getResources().getColor(R.color.white));

            snackView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }*/
    }

    fun hideKeyBoard() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideKeyboard()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }

    fun setAddressData(callBackInterface: CallBackInterface,address:String){
        this.callBackInterface=callBackInterface
        this.address=address

    }

     public interface CallBackInterface {

        fun  onAddressTypeSelected(type:Common.AddressType)
    }
}
