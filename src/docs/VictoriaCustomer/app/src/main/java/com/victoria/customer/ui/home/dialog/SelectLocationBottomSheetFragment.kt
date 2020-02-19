package com.victoria.customer.ui.home.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.widget.FrameLayout
import com.victoria.customer.R
import com.victoria.customer.ui.interfaces.CallbackLocationSelection


class SelectLocationBottomSheetFragment : BottomSheetDialogFragment, View.OnClickListener {


    private lateinit var callbackLocationSelection: CallbackLocationSelection
    lateinit var textViewLabelHome: AppCompatTextView
    lateinit var textViewHome: AppCompatTextView
    lateinit var textViewLocationOnmap: AppCompatTextView
    lateinit var textViewAddFavouritePlace: AppCompatTextView
    //Bottom Sheet Callback
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    constructor()
    @SuppressLint("ValidFragment")
    constructor(callbackLocationSelection: CallbackLocationSelection) {
        this.callbackLocationSelection = callbackLocationSelection
    }

    override fun onStart() {
        super.onStart()

        var dialog = dialog

        if (dialog != null) {
         /*   var bottmSheetView = dialog.findViewById(R.id.designBottomSheet) as View
            bottmSheetView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
*/
            //Set the coordinator layout behavior
          /*  val params = (bottmSheetView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val displaymetrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
            val screenHeight = displaymetrics.heightPixels*/

    /*        val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)*/
            //behavior.state = BottomSheetBehavior.STATE_EXPANDED
           // behavior.peekHeight = 0
        }


    }


    override fun setupDialog(dialog: Dialog, style: Int) {
       // super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.bottom_sheet_select_location, null)
        dialog.setContentView(contentView)
        val bottomSheet = contentView.findViewById(R.id.frameLayout) as FrameLayout
        val behavior = BottomSheetBehavior.from<FrameLayout>(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = 1000

        contentView.requestLayout()

        textViewLabelHome = contentView.findViewById(R.id.textViewLabelHome) as AppCompatTextView
        textViewHome = contentView.findViewById(R.id.textViewHome) as AppCompatTextView
        textViewAddFavouritePlace = contentView.findViewById(R.id.textViewAddFavouritePlace) as AppCompatTextView
        textViewLocationOnmap = contentView.findViewById(R.id.textViewLocationOnmap) as AppCompatTextView



        textViewLabelHome.setOnClickListener(this::onClick)
        textViewHome.setOnClickListener(this::onClick)
        textViewAddFavouritePlace.setOnClickListener(this::onClick)
        textViewLocationOnmap.setOnClickListener(this::onClick)

    }
   /* override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView= inflater.inflate(R.layout.bottom_sheet_select_location, container, true)

        textViewLabelHome = contentView.findViewById(R.id.textViewLabelHome) as AppCompatTextView
        textViewHome = contentView.findViewById(R.id.textViewHome) as AppCompatTextView
        textViewAddFavouritePlace = contentView.findViewById(R.id.textViewAddFavouritePlace) as AppCompatTextView
        textViewLocationOnmap = contentView.findViewById(R.id.textViewLocationOnmap) as AppCompatTextView



        textViewLabelHome.setOnClickListener(this::onClick)
        textViewHome.setOnClickListener(this::onClick)
        textViewAddFavouritePlace.setOnClickListener(this::onClick)
        textViewLocationOnmap.setOnClickListener(this::onClick)
        return contentView
    }*/


  /*  override fun setupDialog(dialog: Dialog, style: Int) {

        //super.setupDialog(dialog, style);
        //Get the content View
        val contentView = View.inflate(context, R.layout.bottom_sheet_select_location, null)
        dialog.setContentView(contentView)


    }*/

    override fun onClick(view: View?) {

        when (view?.id) {

            R.id.textViewLabelHome -> {
                callbackLocationSelection.onHomeSelected(textViewLabelHome?.text.toString(),0.0,0.0)
                dismiss()
            }
            R.id.textViewHome -> {
                callbackLocationSelection.onHomeSelected(textViewHome?.text.toString(),0.0,0.0)
                dismiss()

            }
            R.id.textViewLocationOnmap -> {
                callbackLocationSelection.onSetOnMapSelected()
                dismiss()

            }
            R.id.textViewAddFavouritePlace -> {
                callbackLocationSelection.onFavoritePlaceSelected()
                dismiss()

            }

        }
    }

}