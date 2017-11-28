package com.ibtikar.a3arfnii.di.component

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.ibtikar.a3arfnii.di.modules.ApplicationModule
import com.ibtikar.a3arfnii.di.modules.NetworkModule
import com.ibtikar.a3arfnii.di.scopes.ApplicationScope
import com.ibtikar.a3arfnii.model.localDataProvider.DataManager
import com.squareup.picasso.Picasso
import dagger.Component

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {

    fun provideContext(): Context

    fun providePicasso(): Picasso

    fun provideDataManager(): DataManager

    fun provideFirebaseStorageReference():StorageReference

    fun provideFirebaseDatabase():FirebaseDatabase

}