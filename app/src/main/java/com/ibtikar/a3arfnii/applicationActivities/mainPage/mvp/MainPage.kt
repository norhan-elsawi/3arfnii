package com.ibtikar.a3arfnii.applicationActivities.mainPage.mvp

import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.SyncStateContract
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.ibtikar.a3arfnii.R
import com.ibtikar.a3arfnii.applicationActivities.mainPage.di.DaggerMainPageComponent
import com.ibtikar.a3arfnii.applicationActivities.mainPage.di.MainPageModule
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp.ChooseAreaActivity
import com.ibtikar.a3arfnii.di.Injector
import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.model.utils.Constants
import kotlinx.android.synthetic.main.activity_main_page.*
import javax.inject.Inject


class MainPage : AppCompatActivity(), MainPageContract.View {

    @Inject
    lateinit var presenter: MainPageContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        resolveDaggerDependency(Injector.INSTANCE.appComponent)
        val filter = IntentFilter()
        filter.addAction(Constants.RECEIVE_NETWORK_STATE_ACTION)
        registerReceiver(networkStateReceiver, filter)

        requestAuth()
        handleActions()


    }

    private fun requestAuth() {

        if(presenter.getUserId()=="") {
            val isNetworkAvailableFlag = presenter?.isNetworkConnected()
            if (isNetworkAvailableFlag!!) {
                presenter?.requestAuth()!!
            } else {
                showInternetError()
            }
        }

    }

    private fun handleActions() {
        add.setOnClickListener { _ ->
            if(presenter.getUserId()!="") {
                val intent = Intent(this, ChooseAreaActivity::class.java)
                startActivity(intent)
            }else{
                showInternetError()
            }
        }
    }

    override fun showInternetError() {
        Toast.makeText(this, getString(R.string.open_internet_connection), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateReceiver)
    }

    private fun resolveDaggerDependency(appComponent: ApplicationComponent) {
        DaggerMainPageComponent
                .builder()
                .applicationComponent(appComponent)
                .mainPageModule(MainPageModule(this))
                .build()
                .inject(this)
    }

    private val networkStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("MainPage", "Network "+" connected")
            requestAuth()
        }

    }


}
