package com.ibtikar.a3arfnii.model.localDataProvider

import android.content.Context
import com.ibtikar.a3arfnii.model.utils.Constants

class DataManager( var mContext: Context, var mSharedPrefsHelper: PrefHelper) {

    fun saveUserId(id: String) {
        mSharedPrefsHelper.put(Constants.USER_ID, id)
    }

    fun getUserId():String? {
      return  mSharedPrefsHelper.get(Constants.USER_ID, "")
    }
}

