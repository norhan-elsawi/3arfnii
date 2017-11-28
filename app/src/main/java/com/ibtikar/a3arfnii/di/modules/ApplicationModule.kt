package com.ibtikar.a3arfnii.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ibtikar.a3arfnii.di.scopes.ApplicationScope
import com.ibtikar.a3arfnii.model.localDataProvider.DataManager
import com.ibtikar.a3arfnii.model.localDataProvider.PrefHelper
import com.ibtikar.a3arfnii.model.utils.Constants
import dagger.Module
import dagger.Provides


@Module
class ApplicationModule( var mApplication: Application) {

    @Provides
    @ApplicationScope
    fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @ApplicationScope
    fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationScope
    fun provideSharedPrefs(): SharedPreferences {
        return mApplication.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }


    @Provides
    @ApplicationScope
    fun provideSharedPrefsHelper(sharedPreferences: SharedPreferences): PrefHelper {
        return PrefHelper(sharedPreferences)
    }

    @Provides
    @ApplicationScope
    fun provideDataManager(context: Context, sharedPrefsHelper: PrefHelper): DataManager {
        return DataManager(context, sharedPrefsHelper)
    }


}