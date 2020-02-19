package com.victoria.customer.util

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.dialog_progress.view.*
import android.content.DialogInterface
import android.support.v4.app.FragmentManager


class MyCustomProgressDialog : DialogFragment() {

    val FRAGMENT_TAG = "LoadingFragment"
    private var ivLoading: ImageView? = null
    var rootView: View? = null
    var isZoomIn = true
    lateinit var zoomin: Animation
    lateinit var zoomout: Animation
    public var isShown = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(com.victoria.customer.R.layout.dialog_progress, container, false)

        if (rootView != null) {
            /*ivLoading = rootView!!.findViewById(R.id.iv_loading) as ImageView
            ivLoading!!.setBackgroundResource(R.drawable.anim_drawable)*/
        }

        return rootView
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


    override fun show(manager: FragmentManager, tag: String) {
        if (isShown) return
        super.show(manager, tag)
        isShown = true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        isShown = false
        super.onDismiss(dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isShown = true
        zoomin = AnimationUtils.loadAnimation(context, com.victoria.customer.R.anim.zoom_in);
        zoomout = AnimationUtils.loadAnimation(context, com.victoria.customer.R.anim.zoom_out);

        if (::zoomIn.isInitialized) {
            rootView?.imageView!!.animation = zoomIn
        }
        if (::zoomout.isInitialized) {
            rootView?.imageView!!.animation = zoomout
        }

        zoomin.setAnimationListener(object : AnimationListener {

            override fun onAnimationStart(arg0: Animation) {}

            override fun onAnimationRepeat(arg0: Animation) {}

            override fun onAnimationEnd(arg0: Animation) {
                rootView?.imageView!!.startAnimation(zoomout)
            }
        })

        zoomout.setAnimationListener(object : AnimationListener {

            override fun onAnimationStart(arg0: Animation) {}

            override fun onAnimationRepeat(arg0: Animation) {}

            override fun onAnimationEnd(arg0: Animation) {
                rootView?.imageView!!.startAnimation(zoomin)

            }
        })
    }

    private lateinit var zoomIn: Animation

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {

            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(this.context!!,
                    com.victoria.customer.R.drawable.background_white))
            dialog.setCancelable(false)
            /*Glide.with(Objects.requireNonNull<Context>(context))
                    .load(R.drawable.loading_view)
                    .thumbnail(Glide.with(Objects.requireNonNull<Context>(getContext()))
                            .load(R.drawable.loading_view))
                    .into(rootView?.imageView!!)*/
        }
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }
}
