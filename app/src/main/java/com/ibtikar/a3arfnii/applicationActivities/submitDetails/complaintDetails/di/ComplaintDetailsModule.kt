package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.di

import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.ComplaintDetailsContract
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.ComplaintDetailsModel
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.ComplaintDetailsPresenter
import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ComplaintDetailsModule(private val view: ComplaintDetailsContract.View) {

    @ActivityScope
    @Provides
    internal fun provideView(): ComplaintDetailsContract.View {
        return view
    }

    @ActivityScope
    @Provides
    internal fun provideModel(model: ComplaintDetailsModel): ComplaintDetailsContract.Model {
        return model
    }

    @ActivityScope
    @Provides
    internal fun providePresenter(presenter: ComplaintDetailsPresenter): ComplaintDetailsContract.Presenter {
        return presenter
    }
}