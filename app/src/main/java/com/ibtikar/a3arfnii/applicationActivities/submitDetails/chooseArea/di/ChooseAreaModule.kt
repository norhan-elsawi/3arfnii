package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.di

import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp.ChooseAreaContract
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp.ChooseAreaModel
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp.ChooseAreaPresenter
import dagger.Module
import dagger.Provides


@Module
class ChooseAreaModule( var view: ChooseAreaContract.View) {

    @ActivityScope
    @Provides
    internal fun provideView():  ChooseAreaContract.View {
        return view
    }

    @ActivityScope
    @Provides
    internal fun provideModel(model: ChooseAreaModel): ChooseAreaContract.Model {
        return model
    }

    @ActivityScope
    @Provides
    internal fun providePresenter(presenter: ChooseAreaPresenter): ChooseAreaContract.Presenter {
        return presenter
    }



}