package com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp

import com.ibtikar.a3arfnii.base.implementation.IBasePresenterImpl
import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import javax.inject.Inject


@ActivityScope
class MainPagePresenter @Inject constructor(var view: MainPageContract.View, var model: MainPageContract.Model) : IBasePresenterImpl(), MainPageContract.Presenter {

    override fun getUserId(): String {
        return dataManager.getUserId()!!
    }
}
