package com.victoria.driver.ui.manager


import android.util.Pair
import android.view.View
import com.victoria.driver.di.PerActivity
import com.victoria.driver.ui.base.BaseFragment

import javax.inject.Inject
import javax.inject.Named

/**
 * Created by hlink21 on 25/4/16.
 */
@PerActivity
class FragmentManager @Inject
constructor(private val fragmentManager: android.support.v4.app.FragmentManager,
            @param:Named("placeholder") private val placeHolder: Int) : FragmentHandler {


    override fun openFragment(baseFragment: BaseFragment, option: FragmentHandler.Option, isToBackStack: Boolean, tag: String, sharedElements: List<Pair<View, String>>?) {
        val fragmentTransaction = fragmentManager.beginTransaction()

        // animation
        /*if (option != FragmentHandler.Option.ADD)
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, 0, 0);*/

        when (option) {

            FragmentHandler.Option.ADD -> fragmentTransaction.add(placeHolder, baseFragment, tag)
            FragmentHandler.Option.REPLACE -> fragmentTransaction.replace(placeHolder, baseFragment, tag)
            FragmentHandler.Option.SHOW -> if (baseFragment.isAdded())
                fragmentTransaction.show(baseFragment)
            FragmentHandler.Option.HIDE -> if (baseFragment.isAdded())
                fragmentTransaction.hide(baseFragment)
        }

        if (isToBackStack)
            fragmentTransaction.addToBackStack(tag)

        // shared element Transition
        /*if (sharedElements != null
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && sharedElements.size() > 0) {

            RootFragment currentFragment = (RootFragment) fragmentManager.findFragmentById(placeHolder);

            Transition changeTransform = TransitionInflater.from(currentFragment.getContext()).
                    inflateTransition(R.transition.change_image_transform);

            currentFragment.setSharedElementReturnTransition(changeTransform);
            // currentFragment.setExitTransition(new Fade());

            baseFragment.setSharedElementEnterTransition(changeTransform);
            //baseFragment.setEnterTransition(new Fade());


            for (Pair<View, String> se : sharedElements) {
                fragmentTransaction.addSharedElement(se.first, se.second);
            }
        }*/

        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun showFragment(fragmentToShow: BaseFragment, vararg fragmentToHide: BaseFragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragmentToShow.isAdded) {
            fragmentTransaction.show(fragmentToShow)
            fragmentToShow.onShow()
        } else
            openFragment(fragmentToShow, FragmentHandler.Option.ADD, false, "home", null)

        for (fragment in fragmentToHide) {
            if (fragment.isAdded)
                fragmentTransaction.hide(fragment)
        }
        fragmentTransaction.commit()
    }

    override fun clearFragmentHistory(tag: String?) {
        sDisableFragmentAnimations = true
        if(tag.equals("")) {
            fragmentManager.popBackStackImmediate(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }else{
            fragmentManager.popBackStackImmediate(tag, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

        }
        sDisableFragmentAnimations = false
    }

    companion object {

        var sDisableFragmentAnimations = false
    }


}
