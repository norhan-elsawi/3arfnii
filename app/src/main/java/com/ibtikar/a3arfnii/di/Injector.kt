package com.ibtikar.a3arfnii.di

import com.ibtikar.a3arfnii.application.A3rfnii
import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.di.component.DaggerApplicationComponent
import com.ibtikar.a3arfnii.di.modules.ApplicationModule
import com.ibtikar.a3arfnii.di.modules.NetworkModule


enum class Injector {
    INSTANCE;


    var appComponent: ApplicationComponent?=null


    fun initializeAppComponent(application: A3rfnii): ApplicationComponent {
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .networkModule(NetworkModule())
                .build()
        return appComponent!!
    }
}
