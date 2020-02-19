package com.victoria.driver.ui.location_service;



import com.victoria.driver.di.PerService;
import com.victoria.driver.di.component.ApplicationComponent;

import dagger.Component;

/**
 * Created  on 13/4/17.
 */

@PerService
@Component(dependencies = ApplicationComponent.class)
public interface ServiceComponent {

    void inject(LocationService service);

   /* AuthenticationRepository provideAuthenticationRepository();
    DriverRepository provideDriverRepository();*/
}
