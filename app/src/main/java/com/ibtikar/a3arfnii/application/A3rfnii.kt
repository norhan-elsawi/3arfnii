package com.ibtikar.a3arfnii.application

import android.app.Application
import com.ibtikar.a3arfnii.di.Injector

class A3rfnii : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeApplicationComponent()
    }

    private fun initializeApplicationComponent() {
        if (Injector.INSTANCE.appComponent == null) {
            Injector.INSTANCE.initializeAppComponent(this)
        }
    }

}
