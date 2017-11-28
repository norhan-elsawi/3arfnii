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


    var mAuth: FirebaseAuth? = null

    override fun requestAuth() {
        if (dataManager?.getUserId() == "") {

            mAuth = FirebaseAuth.getInstance()
            mAuth?.signInAnonymously()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    Log.e("auth",user?.uid!!)
                    dataManager.saveUserId(user?.uid!!)

                }

            }
        }
    }

    override fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


}