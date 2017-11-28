package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp

import com.ibtikar.a3arfnii.model.localDataProvider.DataManager
import javax.inject.Inject


class ComplaintDetailsModel @Inject constructor(var dataManager: DataManager) : ComplaintDetailsContract.Model {
    override fun getUserId(): String {

        return dataManager.getUserId()!!
    }

}