package com.victoria.customer.di.module;

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.victoria.customer.di.ViewModelKey
import com.victoria.customer.ui.authentication.viewmodel.*
import com.victoria.customer.ui.home.ride.viewmodel.CancelRideViewModel
import com.victoria.customer.ui.home.settings.viewmodel.ChangePasswordViewModel
import com.victoria.customer.ui.home.settings.viewmodel.EditProfileViewModel
import com.victoria.customer.ui.home.viewmodel.ContactUsViewModel
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import com.victoria.customer.ui.home.viewmodel.HomeViewModel
import com.victoria.customer.ui.home.viewmodel.SelectLocationViewModel
import com.victoria.customer.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindsignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindsignUpViewModel(signInViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtpVerifyViewModel::class)
    abstract fun bindOtpViewModel(otpVerifyViewModel: OtpVerifyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(forgotPasswordViewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindChangePasswordViewModel(changePasswordViewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel::class)
    abstract fun bindContactUsViewModel(contactUsViewModel: ContactUsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindhomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectLocationViewModel::class)
    abstract fun selectLocationViewModel(selectLocationViewModel: SelectLocationViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(FareEstimationViewModel::class)
    abstract fun fareEstimationViewModel(fareEstimationViewModel: FareEstimationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CancelRideViewModel::class)
    abstract fun cancelRideViewModel(cancelRideViewModel: CancelRideViewModel): ViewModel

    /*



     @Binds
     @IntoMap
     @ViewModelKey(SignUpViewModel::class)
     abstract fun bindsignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(OtpViewModel::class)
     abstract fun bindOtpViewModel(OtpViewModel: OtpViewModel): ViewModel


     @Binds
     @IntoMap
     @ViewModelKey(NotificationViewModel::class)
     abstract fun bindNotificationViewModel(notificationViewModel: NotificationViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(HelpViewModel::class)
     abstract fun bindHelpViewModel(helpViewModel: HelpViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(EditProfileViewModel::class)
     abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(ForgetPasswordViewModel::class)
     abstract fun bindForgetPasswordViewModel(forgetPasswordViewModel: ForgetPasswordViewModel): ViewModel


     @Binds
     @IntoMap
     @ViewModelKey(DealerListViewModel::class)
     abstract fun bindDealerListViewModel(dealerListViewModel: DealerListViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(MapTrackingViewModel::class)
     abstract fun bindMapTrackingViewModel(mapTrackingViewModel: MapTrackingViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(SubscriptionViewModel::class)
     abstract fun bindSubscriptionViewModel(subscriptionViewModel: SubscriptionViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(SummaryViewModel::class)
     abstract fun bindSummaryViewModel(summaryViewModel: SummaryViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(ConfirmationViewModel::class)
     abstract fun bindConfirmationViewModel(confirmationViewModel: ConfirmationViewModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(PaymentViewModel::class)
     abstract fun bindPaymentViewModel(paymentViewModel: PaymentViewModel): ViewModel


     @Binds
     @IntoMap
     @ViewModelKey(VehicleOfferViewModel::class)
     abstract fun bindVehicleOfferViewModel(vehicleOfferViewModel: VehicleOfferViewModel): ViewModel
 */

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}