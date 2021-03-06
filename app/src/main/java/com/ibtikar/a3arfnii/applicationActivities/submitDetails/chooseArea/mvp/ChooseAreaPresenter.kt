package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ibtikar.a3arfnii.base.implementation.IBasePresenterImpl
import com.ibtikar.a3arfnii.di.scopes.ActivityScope
import java.util.*
import javax.inject.Inject


@ActivityScope
class ChooseAreaPresenter @Inject constructor(var view: ChooseAreaContract.View,
                                              var model: ChooseAreaContract.Model) : IBasePresenterImpl(), ChooseAreaContract.Presenter {


    @Inject
    lateinit var database: FirebaseDatabase

    var mAuth: FirebaseAuth? = null

    override fun requestAuth() {
        if (dataManager?.getUserId() == "") {

            mAuth = FirebaseAuth.getInstance()
            mAuth?.signInAnonymously()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    Log.e("auth", user?.uid!!)
                    dataManager.saveUserId(user?.uid!!)
                    view.authResult(true)
                }else{
                    view.authResult(false)
                }

            }
        }
    }

    override fun getUserId(): String {
        return model.getUserId()
    }

    override fun getSpinnerValues(): ArrayList<String> {
        var regions: ArrayList<String> = ArrayList()
        if (isNetworkConnected()) {
            var myRef: DatabaseReference = if (Locale.getDefault().language == "ar") {
                database.getReference("regionsArabic")
            } else {
                database.getReference("regionsEnglish")
            }

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.mapTo(regions) { it.getValue(String::class.java)!! }
                    view.returnSpinnerValues(regions)
                    Log.e(TAG, "finally")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                    view.failToGetSpinnerData()
                }
            })
        } else {
            view.failToGetSpinnerData()
        }
        return regions
    }
}