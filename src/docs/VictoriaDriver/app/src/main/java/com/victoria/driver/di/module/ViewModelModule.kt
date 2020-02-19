package com.victoria.driver.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.victoria.driver.di.ViewModelKey
import com.victoria.driver.ui.viewmodel.*
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
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun bindHomeFragmentViewModel(homeFragmentViewModel: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel::class)
    abstract fun bindContactUsViewModelViewModel(contactUsViewModel: ContactUsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CancelRideViewModel::class)
    abstract fun bindCancelRideViewModelViewModel(contactUsViewModel: CancelRideViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}