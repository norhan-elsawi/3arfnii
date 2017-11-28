package com.ibtikar.a3arfnii.di;

import com.ibtikar.a3arfnii.application.A3rfnii;
import com.ibtikar.a3arfnii.di.component.ApplicationComponent;
import com.ibtikar.a3arfnii.di.component.DaggerApplicationComponent;
import com.ibtikar.a3arfnii.di.modules.ApplicationModule;
import com.ibtikar.a3arfnii.di.modules.NetworkModule;


public enum Injector {
    INSTANCE;

    ApplicationComponent applicationComponent;

    public ApplicationComponent initializeAppComponent(A3rfnii application) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .networkModule(new NetworkModule())
                .build();
        return applicationComponent;
    }

    public ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
