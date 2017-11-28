package com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp

import com.ibtikar.a3arfnii.base.interfaces.IBasePresenter

interface MainPageContract {

     interface View {
        fun showInternetError()
    }

    interface Model {
        fun getUserId(): String
    }

    interface Presenter :IBasePresenter{
        fun getUserId(): String
    }
}
