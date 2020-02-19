package com.victoria.customer.di.component;
import com.victoria.customer.di.PerFragment
import com.victoria.customer.di.module.FragmentModule
import com.victoria.customer.ui.authentication.fragments.*
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.fragments.*
import com.victoria.customer.ui.home.ride.fragments.*
import com.victoria.customer.ui.home.ride_history.fragments.PastRideDetailFragment
import com.victoria.customer.ui.home.ride_history.fragments.PastRideFragment
import com.victoria.customer.ui.home.ride_history.fragments.UpcomingRideDetailFragment
import com.victoria.customer.ui.home.ride_history.fragments.UpcomingRideFragment
import com.victoria.customer.ui.home.settings.fragments.ChangePasswordFragment
import com.victoria.customer.ui.home.settings.fragments.ContactUsFragment
import com.victoria.customer.ui.home.settings.fragments.PaymentDetailsFragment
import com.victoria.customer.ui.home.settings.fragments.WebViewFragment
import dagger.Subcomponent


/**
 * Created by hlink21 on 31/5/16.
 */

@PerFragment
@Subcomponent(modules = [(FragmentModule::class)])
interface FragmentComponent {
    fun baseFragment(): BaseFragment
    fun inject(startFragment: StartFragment)
    fun inject(startFragment: SignInFragment)
    fun inject(signupFragment: SignupFragment)
    fun inject(signupVerifyPhoneNumberFragment: SignupVerifyPhoneNumberFragment)
    fun inject(signupBasicDetail1Fragment: SignupBasicDetail1Fragment)
    fun inject(signupBasicDetail2Fragment: SignupBasicDetail2Fragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(myRidesFragment: MyRidesFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(walletFragment: WalletFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(addAmountFragment: AddAmountFragment)
    fun inject(addCreditCardFragment: AddCreditCardFragment) {

    }

    fun inject(transactionHistoryFragment: TransactionHistoryFragment) {

    }

    fun inject(fareEstimationFragment: FareEstimationFragment) {

    }

    fun inject(carAllocationFragment: CarAllocationFragment) {

    }

    fun inject(scheduleRideFragment: ScheduleRideFragment) {

    }

    fun inject(driverComingFragment: DriverComingFragment) {

    }

    fun inject(rideFragment: RideFragment) {

    }

    fun inject(receiptFragment: ReceiptFragment) {

    }

    fun inject(ratingFragment: RatingFragment) {

    }

    fun inject(pastRideFragment: PastRideFragment) {

    }

    fun inject(upcomingRideFragment: UpcomingRideFragment) {

    }

    fun inject(pastRideDetailFragment: PastRideDetailFragment) {

    }

    fun inject(upcomingRideDetailFragment: UpcomingRideDetailFragment) {

    }

    fun inject(changePasswordFragment: ChangePasswordFragment) {

    }

    fun inject(paymentDetailsFragment: PaymentDetailsFragment) {

    }

    fun inject(forgotPasswordFragment: ForgotPasswordFragment) {

    }

    fun inject(cancelRideFragment: CancelRideFragment) {

    }

    fun inject(contactUsFragment: ContactUsFragment) {

    }

    fun inject(webViewFragment: WebViewFragment) {

    }

    fun inject(serviceFragment: ServiceFragment) {

    }

    fun inject(selectLocationFragment: SelectLocationFragment) {

    }

    fun inject(addFavoritePlaceFragment: AddFavoritePlaceFragment) {

    }

    fun inject(homeStartFragment: HomeStartFragment) {

    }

    fun inject(ratingListFragment: RatingListFragment) {

    }

    fun inject(chatMessageListingFragment: ChatMessageListingFragment) {

    }

    fun inject(languageChangeFragment: LanguageChangeFragment) {

    }


}
