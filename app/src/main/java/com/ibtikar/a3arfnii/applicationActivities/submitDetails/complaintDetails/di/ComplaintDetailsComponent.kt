package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.di

import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.ComplaintDetailsActivity
import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(ComplaintDetailsModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface ComplaintDetailsComponent {
    fun inject(activity: ComplaintDetailsActivity)
}
