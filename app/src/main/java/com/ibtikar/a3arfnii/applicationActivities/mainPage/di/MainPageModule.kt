package com.ibtikar.a3arfnii.applicationActivities.mainPage.di

import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp.MainPageContract
import com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp.MainPageModel
import com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp.MainPagePresenter
import dagger.Module
import dagger.Provides

@Module
class MainPageModule(var view: MainPageContract.View) {

    @ActivityScope
    @Provides
    fun provideView(): MainPageContract.View {
        return view
    }

    @ActivityScope
    @Provides
     fun provideModel(model: MainPageModel): MainPageContract.Model {
        return model
    }

    @ActivityScope
    @Provides
    fun providePresenter(presenter: MainPagePresenter): MainPageContract.Presenter {
        return presenter
    }


}