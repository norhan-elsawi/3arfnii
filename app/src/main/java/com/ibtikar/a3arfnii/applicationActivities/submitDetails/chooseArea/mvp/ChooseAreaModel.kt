package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp

import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import com.ibtikar.a3arfnii.model.localDataProvider.DataManager
import javax.inject.Inject

@ActivityScope
class ChooseAreaModel @Inject constructor( var dataManager: DataManager):ChooseAreaContract.Model {

    override fun getUserId(): String {
        return dataManager.getUserId()!!
    }

}