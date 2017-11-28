package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp

import com.ibtikar.a3arfnii.base.interfaces.IBasePresenter


interface ChooseAreaContract {


    interface View {
        fun failToGetSpinnerData()
        fun returnSpinnerValues(regions: ArrayList<String>)
        fun authResult(result:Boolean)
    }

    interface Model {
        fun getUserId(): String
    }

    interface Presenter : IBasePresenter {
        fun getSpinnerValues(): ArrayList<String>
        fun getUserId(): String
        fun requestAuth()

    }
}