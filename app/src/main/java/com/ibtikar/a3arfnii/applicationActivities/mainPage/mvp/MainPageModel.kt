package com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp

import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import com.ibtikar.a3arfnii.model.localDataProvider.DataManager
import javax.inject.Inject


@ActivityScope
class MainPageModel @Inject constructor( var dataManager: DataManager) : MainPageContract.Model {


    override fun getUserId(): String {
        return dataManager.getUserId()!!
    }

}