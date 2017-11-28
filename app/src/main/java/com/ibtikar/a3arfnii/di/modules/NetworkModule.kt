package com.ibtikar.a3arfnii.di.modules

import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ibtikar.a3arfnii.BuildConfig
import com.ibtikar.a3arfnii.di.scopes.ApplicationScope
import com.ibtikar.a3arfnii.model.utils.CacheUtils
import com.ibtikar.a3arfnii.model.utils.Constants
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @ApplicationScope
    fun providePicasso(client: OkHttpClient, app: Application): Picasso {
        val cache = CacheUtils.createDefaultCacheDir(app)
        val picassoOkHttpClient = client
                .newBuilder()
                .cache(Cache(cache, CacheUtils.calculateDiskCacheSize(cache)))
                .build()

        val builder = Picasso.Builder(app)
                .downloader(OkHttp3Downloader(picassoOkHttpClient))
                .listener { picasso, uri, exception -> Log.e("NetworkModule", "Error while loading image $uri") }

        if (BuildConfig.DEBUG) {
            builder.indicatorsEnabled(BuildConfig.DEBUG)
                    .loggingEnabled(BuildConfig.DEBUG)
        }

        return builder.build()
    }

    @Provides
    @ApplicationScope
    fun provideFireBaseStorage(): FirebaseStorage {
        val fireBaseStorage = FirebaseStorage.getInstance()
        fireBaseStorage.maxUploadRetryTimeMillis=2000
        fireBaseStorage.maxOperationRetryTimeMillis=3000
        return fireBaseStorage
    }

    @Provides
    @ApplicationScope
    fun provideFireBaseStorageReference(firebaseStorage:FirebaseStorage): StorageReference {
        return firebaseStorage.reference
    }

    @Provides
    @ApplicationScope
    fun provideFireBaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

}
