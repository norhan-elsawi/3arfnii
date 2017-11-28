package com.ibtikar.a3arfnii.applicationActivities.mainPage.di

import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp.MainPage
import dagger.Component


@ActivityScope
@Component(modules = arrayOf(MainPageModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface MainPageComponent {
    fun inject(activity: MainPage)
}
