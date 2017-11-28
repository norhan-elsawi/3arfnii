package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.di

import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp.ChooseAreaActivity
import dagger.Component


    @ActivityScope
    @Component(modules = arrayOf(ChooseAreaModule::class), dependencies = arrayOf(ApplicationComponent::class))
    interface ChooseAreaComponent {
        fun inject(activity: ChooseAreaActivity)
    }

