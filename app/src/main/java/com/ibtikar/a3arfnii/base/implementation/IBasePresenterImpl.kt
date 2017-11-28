package com.ibtikar.a3arfnii.base.implementation

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.ibtikar.a3arfnii.base.interfaces.IBasePresenter
import com.ibtikar.a3arfnii.model.localDataProvider.DataManager
import javax.inject.Inject


open class IBasePresenterImpl : IBasePresenter {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var dataManager: DataManager



    override fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


}