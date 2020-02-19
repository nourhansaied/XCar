package com.victoria.driver.di.component


import com.victoria.driver.di.PerFragment
import com.victoria.driver.di.module.FragmentModule
import com.victoria.driver.ui.authentication.fragment.*
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.fragment.*
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
    fun inject(forgotPasswordFragment: ForgotPasswordFragment)
    fun inject(signupFragment: SignupFragment)
    fun inject(signUpBasicDetail3Fragment: SignUpVehicleInfoFragment)
    fun inject(uploadVehicleDocumentFragment: UploadVehicleDocumentFragment)
    fun inject(signupVerifyPhoneNumberFragment: SignupVerifyPhoneNumberFragment)
    fun inject(bankAccountInfoFragment: BankAccountInfoFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(driverProfileFragmet: DriverProfileFragmet)
    fun inject(rideHistoryFragment: RideHistoryFragment)
    fun inject(upcomingRideFragment: UpcomingRideFragment)
    fun inject(upcomingRideDetailFragment: UpcomingRideDetailFragment)
    fun inject(pastRideDetailFragment: PastRideDetailFragment)
    fun inject(cancelRideFragment: CancelRideFragment)
    fun inject(pastRideFragment: PastRideFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(changePasswordFragment: ChangePasswordFragment)
    fun inject(webViewFragment: WebViewFragment)
    fun inject(contactUsFragment: ContactUsFragment)
    fun inject(editVehicleDocumentFragment: EditVehicleDocumentFragment)
    fun inject(editBankAccountInfoFragment: EditBankAccountInfoFragment)
    fun inject(editVehicleInfoFragment: EditVehicleInfoFragment)
    fun inject(transactionHistoryFragment: TransactionHistoryFragment)
    fun inject(earningsFragmet: EarningsFragment)
    fun inject(receiptFragment: ReceiptFragment)
    fun inject(ratingFragment: RatingFragment)
    fun inject(chatFragment: ChatFragment)
    fun inject(driverGoingFragment: DriverGoingFragment)
    fun inject(rideStartFragment: RideStartFragment)
    fun inject(changeYourRouteFragment: ChangeYourRouteFragment)
    fun inject(ratingListFragment: RatingListFragment) {

    }

    fun inject(chatMessageListingFragment: ChatMessageListingFragment) {


    }

    fun inject(languageChangeFragment: LanguageChangeFragment) {

    }

}
